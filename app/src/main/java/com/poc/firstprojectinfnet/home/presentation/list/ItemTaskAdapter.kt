package com.poc.firstprojectinfnet.home.presentation.list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.home.data.Task
import java.util.*


class ItemTaskAdapter(
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val photoProfile: Uri?,
    private val onLongClick: (task: Task, view: View) -> Unit,
    onRecyclerViewDataSetChangedx: OnRecyclerViewDataSetChanged
) :
    RecyclerView.Adapter<ItemTaskAdapter.ViewHolder>() {
    private var dataSet: ArrayList<Task>? = null
    private var onRecyclerViewDataSetChanged: OnRecyclerViewDataSetChanged? = null

    private val iconTrash: Drawable =
        recyclerView.resources.getDrawable(R.drawable.baseline_restore_from_trash_24)


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
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
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
                if (data[position].favorite) {
                    holder.imageViewFav.setImageResource(R.drawable.baseline_star_24)
                } else {
                    holder.imageViewFav.setImageResource(R.drawable.baseline_star_border_24)
                }
                Glide.with(context).load(photoProfile).placeholder(R.drawable.avatarprofile)
                    .diskCacheStrategy(DiskCacheStrategy.DATA).centerCrop()
                    .into(holder.imageProfile)

                when (data[position].crit) {
                    "Baixa" -> {
                        holder.marker.setCardBackgroundColor(Color.parseColor("#4169E1"))
                    }
                    "Média" -> {
                        holder.marker.setCardBackgroundColor(Color.parseColor("#3bd309"))
                    }
                    "Alta" -> {
                        holder.marker.setCardBackgroundColor(Color.parseColor("#cd1a31"))
                    }
                }

                holder.itemView.setOnLongClickListener {
                    onLongClick.invoke(data[position], it)
                    true
                }
            }
        }
    }

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageViewFav: ImageView
        val imageProfile: ImageView
        val marker: CardView

        init {
            textView = view.findViewById(R.id.text_task)
            imageViewFav = view.findViewById(R.id.imageViewFav)
            imageProfile = view.findViewById(R.id.imageView)
            marker = view.findViewById(R.id.marker)
        }
    }
}