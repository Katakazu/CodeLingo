package com.example.codelingo.ui.fragments

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codelingo.R
import com.example.codelingo.data.preferences.UserPreferences
import com.google.android.material.card.MaterialCardView

class QuestFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var questAdapter: QuestAdapter
    private lateinit var userPreferences: UserPreferences
    private var quests: MutableList<Quest> = mutableListOf()
    private lateinit var textDailyProgress: TextView
    private lateinit var textDailyPercent: TextView
    private lateinit var progressBar: android.widget.ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quest, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewQuests)
        textDailyProgress = view.findViewById(R.id.textDailyProgress)
        textDailyPercent = view.findViewById(R.id.textDailyPercent)
        progressBar = view.findViewById(R.id.progressBarDaily)
        userPreferences = UserPreferences(requireContext())
        setupQuests()
        questAdapter = QuestAdapter(quests, userPreferences) { position ->
            claimQuest(position)
            updateDailyProgressUI()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = questAdapter
        updateDailyProgressUI()
        return view
    }

    private fun setupQuests() {
        val streak = userPreferences.getUserDays()
        val lessonToday = userPreferences.getLessonsCompletedToday()
        val todayXp = userPreferences.getTodayXp()
        val claimed = userPreferences.getClaimedQuests()
        
        // Debug print
        android.util.Log.d("QuestFragment", "Today XP: $todayXp, Lesson Today: $lessonToday, Streak: $streak")
        
        quests.clear()
        quests.add(
            Quest(
                id = "lesson_1",
                title = "Selesaikan 1 pelajaran hari ini",
                progress = lessonToday,
                target = 1,
                rewardXp = 30,
                claimed = claimed.contains("lesson_1")
            )
        )
        quests.add(
            Quest(
                id = "streak_3",
                title = "Pertahankan streak 3 hari",
                progress = streak,
                target = 3,
                rewardXp = 50,
                claimed = claimed.contains("streak_3")
            )
        )
        quests.add(
            Quest(
                id = "xp_100",
                title = "Dapatkan 100 XP hari ini",
                progress = todayXp,
                target = 100,
                rewardXp = 40,
                claimed = claimed.contains("xp_100")
            )
        )
    }

    private fun claimQuest(position: Int) {
        val quest = quests[position]
        if (!quest.claimed && quest.progress >= quest.target) {
            quest.claimed = true
            userPreferences.addXp(quest.rewardXp)
            userPreferences.addClaimedQuest(quest.id)
            questAdapter.notifyItemChanged(position)
        }
    }

    private fun updateDailyProgressUI() {
        val total = quests.size
        val done = quests.count { it.claimed }
        val percent = (done * 100) / total
        textDailyProgress.text = "$done/$total Quest Selesai"
        textDailyPercent.text = "$percent%"
        progressBar.progress = percent
    }
}

// Data class for Quest
data class Quest(
    val id: String,
    val title: String,
    val progress: Int,
    val target: Int,
    val rewardXp: Int,
    var claimed: Boolean = false
)

// Adapter for RecyclerView
class QuestAdapter(
    private val quests: List<Quest>,
    private val userPreferences: UserPreferences,
    private val onClaim: (Int) -> Unit
) : RecyclerView.Adapter<QuestAdapter.QuestViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quest_row, parent, false)
        return QuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        holder.bind(quests[position], onClaim)
    }

    override fun getItemCount(): Int = quests.size

    inner class QuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card: MaterialCardView = itemView as MaterialCardView
        private val title: TextView = itemView.findViewById(R.id.questTitle)
        private val progress: TextView = itemView.findViewById(R.id.questProgress)
        private val reward: TextView = itemView.findViewById(R.id.questReward)
        private val btnClaim: Button = itemView.findViewById(R.id.btnClaim)

        fun bind(quest: Quest, onClaim: (Int) -> Unit) {
            title.text = quest.title
            progress.text = "Progress: ${quest.progress}/${quest.target}"
            reward.text = "+${quest.rewardXp} XP"
            if (quest.claimed) {
                btnClaim.isEnabled = false
                btnClaim.text = "Selesai"
                btnClaim.setBackgroundResource(R.drawable.button_gray_rounded)
                card.alpha = 0.5f
                title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                btnClaim.isEnabled = quest.progress >= quest.target
                btnClaim.text = "Klaim"
                if (quest.progress >= quest.target) {
                    btnClaim.setBackgroundResource(R.drawable.button_orange_rounded)
                } else {
                    btnClaim.setBackgroundResource(R.drawable.button_gray_rounded)
                }
                card.alpha = 1f
                title.paintFlags = title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                btnClaim.setOnClickListener {
                    onClaim(adapterPosition)
                }
            }
        }
    }
} 