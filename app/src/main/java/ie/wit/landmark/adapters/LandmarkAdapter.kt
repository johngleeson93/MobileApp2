package ie.wit.landmark.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import ie.wit.landmark.models.LandmarkModel
import ie.wit.landmark.utils.customTransformation
import landmark.databinding.CardLandmarkBinding

interface LandmarkClickListener {
    fun onLandmarkClick(landmark: LandmarkModel)
}

class LandmarkAdapter constructor(private var landmarks: ArrayList<LandmarkModel>,
                                  private val listener: LandmarkClickListener,
                                  private val readOnly: Boolean)
    : RecyclerView.Adapter<LandmarkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardLandmarkBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val landmark = landmarks[holder.adapterPosition]
        holder.bind(landmark,listener)
    }

    fun removeAt(position: Int) {
        landmarks.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = landmarks.size

    inner class MainHolder(val binding : CardLandmarkBinding, private val readOnly : Boolean) :
            RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(landmark: LandmarkModel, listener: LandmarkClickListener) {
            binding.root.tag = landmark
            binding.landmark = landmark

            Picasso.get().load(landmark.profilepic.toUri())
                    .resize(200, 200)
                    .transform(customTransformation())
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onLandmarkClick(landmark) }
            binding.executePendingBindings()
        }
    }
}