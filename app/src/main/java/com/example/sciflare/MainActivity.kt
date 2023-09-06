package com.example.sciflare

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.sciflare.databinding.ActivityMainBinding
import com.example.sciflare.databinding.DialogLayoutBinding
import com.example.sciflare.network.RequestBodies
import com.example.sciflare.repository.UserRepository
import com.example.sciflare.util.Resource
import com.example.sciflare.viewmodel.UserViewModel
import com.example.sciflare.viewmodel.ViewModelProviderFactory
import com.example.sciflare.databinding.ItemUsersBinding
import com.example.sciflare.model.UserDetails

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: DialogLayoutBinding
    private var dialogBuilder: AlertDialog.Builder? = null
    private var alertDialog: AlertDialog? = null
    private val userDetailsAdapter: UserDetailsAdapter by lazy { UserDetailsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            showInmateAlertDialog()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = userDetailsAdapter

        setupViewModel()
        binding.map.setOnClickListener {
            startActivity(Intent(applicationContext, MapActivity::class.java))
        }
    }

    private fun setupViewModel() {
        val repository = UserRepository()
        val factory = ViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        getUserDetails()
    }

    private fun showInmateAlertDialog() {
        dialogBuilder = AlertDialog.Builder(this@MainActivity)
        dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
        dialogBuilder!!.setView(dialogBinding.root)
        alertDialog = dialogBuilder!!.create()
        alertDialog!!.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
        alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.show()
        alertDialog!!.setCanceledOnTouchOutside(false)
        dialogBinding.btnDialog.setOnClickListener { alertDialog!!.dismiss() }

        dialogBinding.btnSubmit.setOnClickListener {
            if (dialogBinding.userName.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter the name", Toast.LENGTH_SHORT).show()
            } else if (dialogBinding.emaiId.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter the email", Toast.LENGTH_SHORT).show()
            } else if (dialogBinding.genderId.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter the gender", Toast.LENGTH_SHORT).show()
            } else {
                postUserDetails()
            }
        }
    }

    private fun getUserDetails() {
        viewModel.picsData.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { picsResponse ->
                        userDetailsAdapter.setData(picsResponse)

                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }

                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun postUserDetails() {
        val body = RequestBodies.LoginBody(
            dialogBinding.genderId.text.toString(),
            dialogBinding.emaiId.text.toString(),
            dialogBinding.userName.text.toString()
        )
        viewModel.addUser(body)
        viewModel.postResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let {
                            Toast.makeText(this, "Success fully deleted", Toast.LENGTH_SHORT).show()
                            alertDialog!!.dismiss()
                            startActivity(intent)
                        }
                    }

                    is Resource.Error -> {
                        response.message?.let {
                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                            alertDialog!!.dismiss()
                        }
                    }

                    is Resource.Loading -> {
                        Toast.makeText(this, "Success fully deleted", Toast.LENGTH_SHORT).show()
                        alertDialog!!.dismiss()
                        startActivity(intent)
                    }
                }
            }
        }
    }

    inner class UserDetailsAdapter : RecyclerView.Adapter<UserDetailsAdapter.PicsViewHolder>() {

        private var userDetailList = emptyList<UserDetails>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicsViewHolder {
            val binding =
                ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PicsViewHolder(binding)
        }

        override fun onBindViewHolder(holder: PicsViewHolder, position: Int) {
            holder.binding.image.load("https://yt3.googleusercontent.com/ytc/AOPolaQHa4sJblMTBun6QcMeMok6jXtawy5qYSRwF_-qTQ=s176-c-k-c0x00ffffff-no-rj") {
                transformations(CircleCropTransformation())
                transformations(RoundedCornersTransformation())
            }
            holder.binding.nameId.text = userDetailList[position].name
            holder.binding.emailId.text = userDetailList[position].email
            holder.binding.genderId.text = userDetailList[position].gender

            holder.binding.deleteId.setOnClickListener {
                deleteDetail(userDetailList[position]._id)
            }
        }

        override fun getItemCount(): Int {
            return userDetailList.size
        }

        inner class PicsViewHolder(val binding: ItemUsersBinding) :
            RecyclerView.ViewHolder(binding.root)

        @SuppressLint("NotifyDataSetChanged")
        fun setData(newData: List<UserDetails>) {
            userDetailList = newData
            notifyDataSetChanged()
        }
    }

    fun deleteDetail(deleteId: String) {

        viewModel.deleteUser(deleteId)
        viewModel.deleteResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let {
                            Toast.makeText(this, "Success fully deleted", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                    }

                    is Resource.Error -> {
                        response.message?.let {
                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {
                        Toast.makeText(this, "Success fully deleted", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }
                }
            }
        }
    }
}