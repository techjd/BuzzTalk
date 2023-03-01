package com.ssip.buzztalk.ui.fragments.jobs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssip.buzztalk.databinding.ItemChoicesBinding
import com.ssip.buzztalk.databinding.ItemOpportunitiesBinding
import com.ssip.buzztalk.models.customdata.choices
import com.ssip.buzztalk.models.opportunities.response.AllOppo
import com.ssip.buzztalk.models.opportunities.response.Opportunity
import com.ssip.buzztalk.ui.fragments.post.PostChoiceAdapter
import com.ssip.buzztalk.ui.fragments.post.PostChoiceAdapter.PostChoiceViewHolder

class JobsAdapter(): RecyclerView.Adapter<JobsAdapter.JobsViewHolder>() {

  var opportunities: List<Opportunity> = mutableListOf()

  inner class JobsViewHolder(val itemOpportunitiesBinding: ItemOpportunitiesBinding): RecyclerView.ViewHolder(itemOpportunitiesBinding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
    val binding =
      ItemOpportunitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return JobsViewHolder(binding)
  }

  override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
    val choice = opportunities[position]
    with(holder.itemOpportunitiesBinding){
      userName.text = choice.orgId.organizationName
      textView6.text = choice.orgId.organizationType
      lookingForContent.text = choice.lookingFor
      descContent.text = choice.shortDescription
      requiredSkillsContent.text = choice.requiredSkills
      budgetContent.text = "${choice.budget} ₹"
    }
  }

  override fun getItemCount(): Int {
    return opportunities.size
  }

}