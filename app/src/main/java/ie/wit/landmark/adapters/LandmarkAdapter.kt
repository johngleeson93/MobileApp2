package ie.wit.landmark.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.landmark.R
import ie.wit.landmark.databinding.CardLandmarkBinding
import ie.wit.landmark.models.LandmarkModel

class LandmarkAdapter constructor(private var landmarks: List<LandmarkModel>)
    : RecyclerView.Adapter<LandmarkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardLandmarkBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val landmark = landmarks[holder.adapterPosition]
        holder.bind(landmark)
    }

    override fun getItemCount(): Int = landmarks.size

    inner class MainHolder(val binding : CardLandmarkBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(landmark: LandmarkModel) {
            binding.paymentamount.text = landmark.amount.toString()
            binding.paymentmethod.text = landmark.paymentmethod
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
        }
    }
}