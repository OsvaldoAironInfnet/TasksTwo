package com.poc.firstprojectinfnet.home.presentation


import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.poc.commom.base.views.BaseActivity
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.databinding.ActivityHomeBinding
import com.poc.firstprojectinfnet.home.navigation.RedirectHomeFlowEnum
import org.koin.android.ext.android.inject
import java.text.DateFormatSymbols
import java.util.*

class HomeActivity : BaseActivity(), HomeContract.View {

    private var _binding: ActivityHomeBinding? = null
    private val homeViewModel: HomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)


        setNavigationRoot(R.id.nav_host_fragment)
        supportActionBar?.hide()
        setContentView(_binding?.root)

        homeViewModel.init(this, intent.getBundleExtra("responseData"))

        homeViewModel.recoveryAllTasks()

        val calendar = Calendar.getInstance()

        val dayOfWeek = dayOfWeek(calendar)
        val nameOfMonth = nameOfMonth(calendar)

        _binding?.descriptionHome?.text = "${dayOfWeek?.capitalize()},${nameOfMonth?.capitalize()}"

        _binding?.btnMore?.setOnClickListener {

            val bottomSheet = BottomSheetDialog(this)

            bottomSheet.setContentView(R.layout.bottom_sheet_home)

            val listTask = bottomSheet.findViewById<LinearLayout>(R.id.listtask)
            listAllFavoriteTasks(listTask)

            val listTasks = bottomSheet.findViewById<LinearLayout>(R.id.listAllTasks)
            listAllTasks(listTasks)

            bottomSheet.show()
        }

        _binding?.fab?.setOnClickListener {

            val bottomSheet = BottomSheetDialog(this)

            bottomSheet.setContentView(R.layout.bottom_sheet_add_task)

            bottomSheet.findViewById<ImageButton>(R.id.btn_addtask)?.setOnClickListener {
                homeViewModel.createTask(
                    bottomSheet.findViewById<EditText>(R.id.edt_title)?.text.toString(),
                    bottomSheet.findViewById<EditText>(R.id.edt_description)?.text.toString(),
                    bottomSheet.findViewById<CheckBox>(R.id.checkBoxFav)?.isChecked
                )
            }

            bottomSheet.show()
        }
    }

    private fun dayOfWeek(calendar: Calendar): String? {
        return DateFormatSymbols().weekdays[calendar[Calendar.DAY_OF_WEEK]]
    }

    private fun nameOfMonth(calendar: Calendar): String? {
        return DateFormatSymbols().months[calendar[Calendar.MONTH]]
    }

    private fun removeTasksSelected(view: LinearLayout?) {
        view?.setOnClickListener {


        }
    }

    private fun listAllTasks(view: LinearLayout?) {
        view?.setOnClickListener {
            homeViewModel.listAllTasks()
        }
    }

    private fun listAllFavoriteTasks(view: LinearLayout?) {
        view?.setOnClickListener {
            homeViewModel.listAllFavoriteTasksSelected()
        }
    }

    override fun showMessage(string: String) {}

    override fun redirectToLogin() {}


    override fun redirectToListTask() {
        navigationScreen?.navigate(RedirectHomeFlowEnum.HOME_LIST_FRAGMENT.navigationScreen)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        redirectToListTask()
    }
}