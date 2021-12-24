package ie.wit.landmark.ui.report

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.landmark.adapters.LandmarkAdapter
import ie.wit.landmark.adapters.LandmarkClickListener
import ie.wit.landmark.models.LandmarkModel
import ie.wit.landmark.ui.auth.LoggedInViewModel
import ie.wit.landmark.utils.*
import landmark.R
import landmark.databinding.FragmentReportBinding


class ReportFragment : Fragment(), LandmarkClickListener {

    private var _fragBinding: FragmentReportBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReportBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        fragBinding.fab.setOnClickListener {
            val action = ReportFragmentDirections.actionReportFragmentToLandmarkFragment()
            findNavController().navigate(action)
        }
        showLoader(loader, "Downloading Landmarks")
        reportViewModel.observableLandmarksList.observe(viewLifecycleOwner, Observer { landmarks ->
            landmarks?.let {
                render(landmarks as ArrayList<LandmarkModel>)
                hideLoader(loader)
                checkSwipeRefresh()
            }
        })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader, "Deleting Landmark")
                val adapter = fragBinding.recyclerView.adapter as LandmarkAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                reportViewModel.delete(
                    reportViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as LandmarkModel).uid!!
                )
                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onLandmarkClick(viewHolder.itemView.tag as LandmarkModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
            itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_report, menu)

        val item = menu.findItem(R.id.toggleLandmarks) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        val toggleLandmarks: SwitchCompat = item.actionView.findViewById(R.id.toggleButton)
        toggleLandmarks.isChecked = false

        toggleLandmarks.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) reportViewModel.loadAll()
                else reportViewModel.load()
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    private fun render(landmarksList: ArrayList<LandmarkModel>) {
        fragBinding.recyclerView.adapter = LandmarkAdapter(landmarksList, this,
                                                        reportViewModel.readOnly.value!!)
        if (landmarksList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.landmarksNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.landmarksNotFound.visibility = View.GONE
        }
    }

    override fun onLandmarkClick(landmark: LandmarkModel) {
        val action = ReportFragmentDirections.actionReportFragmentToLandmarkDetailFragment(landmark.uid!!)

        if(!reportViewModel.readOnly.value!!)
                findNavController().navigate(action)
    }

    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader, "Downloading landmarks")
            if(reportViewModel.readOnly.value!!)
                reportViewModel.loadAll()
            else
                reportViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader, "Downloading landmarks")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                reportViewModel.liveFirebaseUser.value = firebaseUser
                reportViewModel.load()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}