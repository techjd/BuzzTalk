package com.ssip.buzztalk.ui.fragments.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssip.buzztalk.databinding.ItemChoicesBinding
import com.ssip.buzztalk.databinding.ItemSearchUserBinding
import com.ssip.buzztalk.models.customdata.choices

class PostChoiceAdapter(
    val change: (pos: Int) -> Unit
): RecyclerView.Adapter<PostChoiceAdapter.PostChoiceViewHolder>() {

    var choices: MutableList<choices> = mutableListOf()

    inner class PostChoiceViewHolder(val itemChoicesBinding: ItemChoicesBinding): RecyclerView.ViewHolder(itemChoicesBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostChoiceViewHolder {
        val binding =
            ItemChoicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostChoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostChoiceViewHolder, position: Int) {
        val choice = choices[position]
        with(holder){
            itemChoicesBinding.choice.text = choice.category
            itemChoicesBinding.root.setOnClickListener {
                change(position)
            }
            itemChoicesBinding.choice.isChecked = choice.isMarked
        }
    }

    override fun getItemCount(): Int {
        return choices.size
    }

    fun changeList(users: MutableList<choices>) {
        choices = users
        notifyDataSetChanged()
    }
}