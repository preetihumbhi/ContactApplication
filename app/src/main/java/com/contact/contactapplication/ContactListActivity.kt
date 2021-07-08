package com.contact.contactapplication

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.contact.contactapplication.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_contactlist.*
import java.security.Permission


class ContactListActivity : BaseActivity() {

    private var mViewModel: ContactRequestModel? = null
    private lateinit var contacts: ArrayList<ContactPojo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactlist)

        mViewModel = ViewModelProvider(this).get(ContactRequestModel::class.java)
        attachObservers()
        contacts = ArrayList<ContactPojo>()
        if (requestContactPermission()) {
            showProgress()
            mViewModel?.getRequestData(this)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        101
                    )

                } else {
                    mViewModel?.getRequestData(this)

                }
            }
        }
    }

    private fun attachObservers() {
        mViewModel?.rquestResponse?.observe(this, Observer {
            it?.let {

                if (it.size > 0) {
                    rv_contact_list.visibility = View.VISIBLE
                    ll_empty.visibility = View.GONE
                    contacts = it
                    setUpdata(it);
                    hideProgress()
                } else {
                    rv_contact_list.visibility = View.GONE
                    ll_empty.visibility = View.VISIBLE
                }

            }
        })

        mViewModel?.apiError?.observe(this, Observer {
            it?.let {
                showSnackBar(it, rv_contact_list)
            }
        })

        mViewModel?.isLoading?.observe(this, Observer {
            showLoading(it)
        })
    }


    private fun setUpdata(contacts: ArrayList<ContactPojo>) {
        rv_contact_list.layoutManager = LinearLayoutManager(this)
        var contactAdapter = ContactListAdapter(this, contacts)
        rv_contact_list.adapter = contactAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.action_one) {

            val builder = AlertDialog.Builder(this)
                .create()
            val view = layoutInflater.inflate(R.layout.dialog_add_contact, null)
            val add = view.findViewById<TextView>(R.id.tv_done)
            val name = view.findViewById<TextView>(R.id.tv_name)
            val number = view.findViewById<TextView>(R.id.tv_number)
            builder.setView(view)
            add.setOnClickListener {

                if (TextUtils.isEmpty(name.text.toString()))
                    showSnackBar("Enter name", name)
                else if (TextUtils.isEmpty(number.text.toString()))
                    showSnackBar("Enter number", number)
                else {

                    var contact = ContactPojo()
                    contact.setName(name.text.toString())
                    contact.setNumber(number.text.toString())
                    contacts.add(0, contact)
                    setUpdata(contacts)
                    builder.dismiss()
                }
            }
            builder.setCanceledOnTouchOutside(false)
            builder.show()

            return true
        }


        return super.onOptionsItemSelected(item)

    }

}


