package com.oo.phonealbum

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPresenter
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), Observer<ArrayList<PhotoDataModel>>,
    Toolbar.OnMenuItemClickListener {



    private val mPhotoRecyclerView:RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.data_list)
    }

    private val mToolBar: Toolbar by lazy {
      findViewById<Toolbar>(R.id.toolBar)

    }

    private val viewModel :PhotoViewModel by lazy {
        val result=ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(PhotoViewModel::class.java)
        result.photoLiveData.observe(this,this)
        result.photoDataState.observe(this,object :Observer<PhotoDataState>{
            override fun onChanged(t: PhotoDataState?) {
                t?.run {
                    when(t){
                        PhotoDataState.SUCCESS->{
                            Toast.makeText(this@MainActivity,"加载成功",Toast.LENGTH_SHORT).show()
                        }
                        PhotoDataState.LOADING->{
                            Toast.makeText(this@MainActivity,"加载中...",Toast.LENGTH_SHORT).show()
                        }
                        PhotoDataState.FAILURE->{
                            Toast.makeText(this@MainActivity,"加载失败",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

        })
        return@lazy result
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.gride -> {
                val gridLayoutManager = GridLayoutManager(this@MainActivity, 2)
                gridLayoutManager.orientation = RecyclerView.VERTICAL
                mPhotoRecyclerView.layoutManager = gridLayoutManager
                mPhotoRecyclerView.adapter?.notifyDataSetChanged()
                return true
            }
            R.id.list -> {
                val linearLayoutManager = LinearLayoutManager(this)
                linearLayoutManager.orientation = RecyclerView.VERTICAL
                mPhotoRecyclerView.layoutManager = linearLayoutManager
                mPhotoRecyclerView.adapter?.notifyDataSetChanged()
                return true
            }
            else->{
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mToolBar.setOnMenuItemClickListener(this)

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }else{
            viewModel.getPhotoDatas(applicationContext)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED) {
           viewModel.getPhotoDatas(this)
        }
    }

    override fun onChanged(t: ArrayList<PhotoDataModel>?) {
        val photoListAdapter = PhotoListAdapter(object : DiffUtil.ItemCallback<PhotoDataModel>() {
            override fun areItemsTheSame(
                oldItem: PhotoDataModel,
                newItem: PhotoDataModel
            ): Boolean {
                return TextUtils.equals(oldItem.uri, newItem.uri)
            }

            override fun areContentsTheSame(
                oldItem: PhotoDataModel,
                newItem: PhotoDataModel
            ): Boolean {
                return TextUtils.equals(oldItem.uri, newItem.uri)
            }

        })


        photoListAdapter.submitList(t)
        mPhotoRecyclerView.layoutManager = GridLayoutManager(this,3,RecyclerView.VERTICAL,false)
        mPhotoRecyclerView.adapter = photoListAdapter
    }

}
