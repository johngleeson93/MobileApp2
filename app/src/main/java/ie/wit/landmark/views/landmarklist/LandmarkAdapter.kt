package ie.wit.landmark.views.landmarklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import landmark.databinding.CardLandmarkBinding
import ie.wit.landmark.models.LandmarkModel

interface LandmarkListener {
    fun onLandmarkClick(landmark: LandmarkModel)
}

class LandmarkAdapter constructor(private var landmarks: List<LandmarkModel>,
                                   private val listener: LandmarkListener
) :
        RecyclerView.Adapter<LandmarkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardLandmarkBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val landmark = landmarks[holder.adapterPosition]
        holder.bind(landmark, listener)
    }

    override fun getItemCount(): Int = landmarks.size

    class MainHolder(private val binding : CardLandmarkBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(landmark: LandmarkModel, listener: LandmarkListener) {
            binding.landmarkTitle.text = landmark.title
            binding.description.text = landmark.description
            if (landmark.image != ""){
                Picasso.get()
                    .load(landmark.image)
                    .resize(200, 200)
                    .into(binding.imageIcon)
            }
            binding.root.setOnClickListener { listener.onLandmarkClick(landmark) }
        }
    }
}
