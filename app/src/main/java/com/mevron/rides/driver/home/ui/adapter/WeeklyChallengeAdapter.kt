package com.mevron.rides.driver.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.WeeklyItemBinding
import com.mevron.rides.driver.home.data.model.home.WeeklyChallenge

class WeeklyChallengeAdapter :
    ListAdapter<WeeklyChallenge, WeeklyChallengeAdapter.WeeklyChallengeHolder>(
        WeeklyDiffUtil()
    ) {
    class WeeklyDiffUtil : DiffUtil.ItemCallback<WeeklyChallenge>() {
        override fun areItemsTheSame(oldItem: WeeklyChallenge, newItem: WeeklyChallenge): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: WeeklyChallenge,
            newItem: WeeklyChallenge
        ): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }

    }

    class WeeklyChallengeHolder(val binding: WeeklyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyChallengeHolder {
        return WeeklyChallengeHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.weekly_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeeklyChallengeHolder, position: Int) {
        val challenge = getItem(position)
        holder.binding.weeklyGoal.text = "Earn  ${challenge.weekly_goal}/week"
        holder.binding.endDay.text = challenge.expiry_date
        holder.binding.earnedAmount.text =
            "${challenge.earned_goal} earned out of ${challenge.weekly_goal}"
        holder.binding.weeklyGoalText.text = challenge.description
    }
}