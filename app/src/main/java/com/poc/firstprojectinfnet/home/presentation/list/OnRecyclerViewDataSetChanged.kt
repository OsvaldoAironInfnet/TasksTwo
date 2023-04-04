package com.poc.firstprojectinfnet.home.presentation.list

import com.poc.firstprojectinfnet.home.data.Task

interface OnRecyclerViewDataSetChanged {
    fun onDataSetChanged(position: Int)
    fun onItemRemoved(task: Task?)
}