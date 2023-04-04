package com.poc.firstprojectinfnet.home.presentation.list

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.home.data.Task


class ItemTaskAdapter(
    private val recyclerView: RecyclerView,
    onRecyclerViewDataSetChangedx: OnRecyclerViewDataSetChanged
) :
    RecyclerView.Adapter<ItemTaskAdapter.ViewHolder>() {
    private var dataSet: ArrayList<Task>? = null
    private var onRecyclerViewDataSetChanged: OnRecyclerViewDataSetChanged? = null

    private val iconTrash: Drawable = recyclerView.resources.getDrawable(R.drawable.baseline_restore_from_trash_24)


    init {
        dataSet = ArrayList<Task>()
        onRecyclerViewDataSetChanged = onRecyclerViewDataSetChangedx
        addSwipeAndDeleteTask()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataSet!!.size


    private fun addSwipeAndDeleteTask() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task: Task? = dataSet?.get(viewHolder.adapterPosition)
                dataSet?.removeAt(viewHolder.adapterPosition)

                notifyItemRemoved(viewHolder.adapterPosition)

                Snackbar.make(recyclerView, "Tarefa Excluida", Snackbar.LENGTH_SHORT)
                    .setAction(
                        "Fechar",
                        View.OnClickListener {}).show()

                onRecyclerViewDataSetChanged?.onItemRemoved(task = task)

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                /**
                val itemView = viewHolder.itemView;
                val itemViewHeight = itemView.height

                val background = ColorDrawable(Color.RED)

                val p = Paint()
                p.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                val mBackGround = ColorDrawable()
                val backgroundColor = Color.parseColor("#b80f0a")
                val iconTrash = ContextCompat.getDrawable(recyclerView.context, R.drawable.baseline_restore_from_trash_24)
                val intrinsicWidth = iconTrash?.intrinsicWidth
                val intrinsicHeight = iconTrash?.intrinsicHeight

                val isCancelled = dX == 0F && !isCurrentlyActive
                if(isCancelled) {
                    c.drawRect(itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat(), p)
                    return
                }

                if(dX < 0 && dX < -40) {

                    val deleteIconTop = itemView.top + (itemViewHeight - intrinsicHeight!!) / 2
                    val deleteIconMargin = (itemViewHeight - intrinsicHeight) / 2
                    val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
                    val deleteIconRight = itemView.right - deleteIconMargin
                    val deleteIconBottom = deleteIconTop - intrinsicHeight


                    iconTrash.setBounds(24,
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    iconTrash.draw(c)
                }
                */
            }

        }).attachToRecyclerView(recyclerView)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(task: Task) {
        dataSet?.add(task)
        onRecyclerViewDataSetChanged?.onDataSetChanged(dataSet!!.size)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        dataSet?.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataSet?.let { data ->
            if (data[position].title == "") {
                holder.itemView.visibility = View.GONE
            } else {
                holder.textView.text = data[position].title
                holder.textViewDescription.text = data[position].description
                if (data[position].isFavorite) {
                    holder.imageViewFav.setImageResource(R.drawable.baseline_star_24)
                } else {
                    holder.imageViewFav.setImageResource(R.drawable.baseline_star_border_24)
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textView: TextView
        val textViewDescription: TextView
        val imageViewFav: ImageView

        init {
            textView = view.findViewById(R.id.text_task)
            textViewDescription = view.findViewById(R.id.text_description_task)
            imageViewFav = view.findViewById(R.id.imageViewFav)
        }


    }
}