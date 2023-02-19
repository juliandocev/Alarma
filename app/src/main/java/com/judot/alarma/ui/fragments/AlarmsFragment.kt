package com.judot.alarma.ui.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.judot.alarma.R
import com.judot.alarma.adapters.AlarmsAdapter
import com.judot.alarma.data.db.entities.Alarm
import com.judot.alarma.databinding.FragmentAlarmsBinding
import com.judot.alarma.ui.AlarmsViewModel
import com.judot.alarma.ui.MainActivity
import kotlin.concurrent.fixedRateTimer

class AlarmsFragment: Fragment(R.layout.fragment_alarms), AlarmsAdapter.Communicator {
    //private var _binding: FragmentAlarmsBinding? = null
    lateinit var viewModel: AlarmsViewModel
    lateinit var alarmsAdapter: AlarmsAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        viewModel.getAllAlarms().observe(viewLifecycleOwner, Observer { alarms->

            alarmsAdapter.differ.submitList(alarms)

        })


        /** Gesture management
         * Showing delete button on left swipe*/
        val itemTouchHelperCallback = object : ItemTouchHelper.Callback(){

            private val limitScrollX = dipToPx(100f, activity as MainActivity)
            private var currentScrollX = 0
            private var currentScrollXWhenInActive = 0
            private var initXWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {

                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    if(dX == 0f){
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive = true
                    }

                    if(isCurrentlyActive){
                        // swipe with finger

                        var scrollOffSet = currentScrollX + (-dX).toInt()
                        if(scrollOffSet > limitScrollX){
                            scrollOffSet = limitScrollX
                        }
                        else if(scrollOffSet < 0){
                            scrollOffSet = 0
                        }

                        viewHolder.itemView.scrollTo(scrollOffSet,0)
                    }

                    else{

                        // swipe with auto animation
                        if(firstInActive){
                            firstInActive = false
                            currentScrollXWhenInActive = viewHolder.itemView.scrollX
                            initXWhenInActive =dX
                        }
                        if(viewHolder.itemView.scrollX < limitScrollX){
                            viewHolder.itemView.scrollTo((currentScrollXWhenInActive * dX / initXWhenInActive).toInt(),0)
                        }

                    }
                }

            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                if(viewHolder.itemView.scrollX > limitScrollX){
                    viewHolder.itemView.scrollTo(limitScrollX,0)
                }
                else if (viewHolder.itemView.scrollX < 0){
                    viewHolder.itemView.scrollTo(0,0)
                }
            }

        }

        val recyclerView = activity?.findViewById<RecyclerView>(R.id.rvAlarms)
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recyclerView)


        }


        val fbAddAlarms = getActivity()?.findViewById<View>(R.id.fbAddAlarms)
        fbAddAlarms?.setOnClickListener(){
            val setAlarmFragment = SetAlarmFragment()
            val transaction : FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            transaction.replace(R.id.AppNavHostFragment, setAlarmFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }
        //TODO napravi recycle viewto kato v shoppingList aplikaciata. No purvo proveri dali pravi save kato horata



    }


    private fun setupRecyclerView(){
        // find the rv for this fragment
        val recyclerView = getActivity()?.findViewById<RecyclerView>(R.id.rvAlarms)
        // initiate the news adapter ofr the item_article_preview
        alarmsAdapter = AlarmsAdapter( viewModel, this)
        // Attach the adapter and the layout manager to the recycler view of the fragment
        recyclerView?.apply{
            adapter = alarmsAdapter
            layoutManager = LinearLayoutManager(activity)
            //addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

    private fun dipToPx(dipValue: Float, context: Context): Int{
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }



    // pass data from alarms to adapter
    override fun passDataCom(item: Alarm) {
        val bundle =Bundle().apply {
            putSerializable("alarm", item)}

        val setAlarmFragment = SetAlarmFragment(false)
        setAlarmFragment.arguments = bundle
        val transaction : FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        transaction.replace(R.id.AppNavHostFragment, setAlarmFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



}