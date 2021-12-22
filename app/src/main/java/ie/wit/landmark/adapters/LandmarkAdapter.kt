package ie.wit.landmark.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.landmark.models.LandmarkModel
import landmark.R
import landmark.databinding.CardLandmarkBinding

class LandmarkAdapter constructor(private var landmarks: List<LandmarkModel>)
    : RecyclerView.Adapter<LandmarkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardLandmarkBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val donation = landmarks[holder.adapterPosition]
        holder.bind(donation)
    }

    override fun getItemCount(): Int = landmarks.size

    inner class MainHolder(val binding : CardLandmarkBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(donation: LandmarkModel) {
            binding.paymentamount.text = donation.amount.toString()
            binding.paymentmethod.text = donation.paymentmethod
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
        }
    }
}