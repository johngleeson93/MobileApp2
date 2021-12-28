package ie.wit.landmark.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.landmark.ui.auth.LoggedInViewModel
import ie.wit.landmark.ui.report.ReportViewModel
import landmark.databinding.FragmentLandmarkDetailBinding
import timber.log.Timber

class LandmarkDetailFragment : Fragment() {

    private lateinit var detailViewModel: LandmarkDetailViewModel
    private val args by navArgs<LandmarkDetailFragmentArgs>()
    private var _fragBinding: FragmentLandmarkDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ReportViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentLandmarkDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this)[LandmarkDetailViewModel::class.java]
        detailViewModel.observableLandmark.observe(viewLifecycleOwner, { render() })

        fragBinding.editLandmarkButton.setOnClickListener {
            detailViewModel.updateLandmark(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.landmarkid, fragBinding.landmarkvm?.observableLandmark!!.value!!)
            //Force Reload of list to guarantee refresh
            reportViewModel.load()
            findNavController().navigateUp()
            //findNavController().popBackStack()

                }

        fragBinding.deleteLandmarkButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                            detailViewModel.observableLandmark.value?.uid!!)
            findNavController().navigateUp()
        }
        return root
    }

    private fun render() {
        fragBinding.landmarkvm = detailViewModel
        Timber.i("Retrofit fragBinding.landmarkvm == $fragBinding.landmarkvm")
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getLandmark(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                        args.landmarkid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}