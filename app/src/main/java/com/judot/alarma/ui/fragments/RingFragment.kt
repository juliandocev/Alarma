package com.judot.alarma.ui.fragments

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.judot.alarma.R
import com.judot.alarma.adapters.AlarmsAdapter
import com.judot.alarma.databinding.FragmentAlarmsBinding
import com.judot.alarma.databinding.FragmentRingBinding
import com.judot.alarma.services.AlarmService
import com.judot.alarma.ui.AlarmsViewModel

class RingFragment : Fragment(R.layout.fragment_ring) {

    private var _binding: FragmentRingBinding? = null
//    lateinit var viewModel: AlarmsViewModel
//    lateinit var alarmsAdapter: AlarmsAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRingBinding.bind(view)


        binding.activityRingDismiss.setOnClickListener{
            val intentService = Intent(activity?.applicationContext, AlarmService::class.java)
            activity?.applicationContext?.stopService(intentService)
            activity?.onBackPressed() // Is this the right one for closing the fragment

            // Remove fragment
//            val fragment = activity?.supportFragmentManager?.findFragmentById(R.layout.fragment_ring)
//            if (fragment != null) {
//                activity?.supportFragmentManager?.beginTransaction()?.remove(fragment)?.commit()
//            }
        }

        animateClock()
    }

    private fun animateClock() {
        val clock: ImageView = binding.activityRingClock
        val rotateAnimation = ObjectAnimator.ofFloat(clock, "rotation", 0f, 20f, 0f, -20f, 0f)
        rotateAnimation.repeatCount = ValueAnimator.INFINITE
        rotateAnimation.duration = 800
        rotateAnimation.start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}