package ie.wit.landmark.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import ie.wit.landmark.R

class LandmarkDetailFragment : Fragment() {

    companion object {
        fun newInstance() = LandmarkDetailFragment()
    }

    private lateinit var viewModel: LandmarkDetailViewModel
    private val args by navArgs<LandmarkDetailFragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_landmark_detail, container, false)

        Toast.makeText(context,"Landmark ID Selected : ${args.landmarkid}",Toast.LENGTH_LONG).show()

        return view
    }


}