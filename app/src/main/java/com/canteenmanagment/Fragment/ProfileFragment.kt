package com.canteenmanagment.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.canteenmanagment.MainActivity
import com.canteenmanagment.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        //var a= 0/1

        FirebaseAuth.getInstance().currentUser.let { firebaseUser ->
            binding.TVEmail.text = firebaseUser?.email
            binding.TVName.text = firebaseUser?.displayName
        }

        binding.TVLogout.setOnClickListener {
            var mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            var i = Intent(activity?.applicationContext, MainActivity::class.java)

            startActivity(i)
            activity?.finish()
        }

        val versionName = context!!.packageManager
            .getPackageInfo(context!!.packageName, 0).versionName

        binding.TVVersion.text = "V" + versionName






        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}