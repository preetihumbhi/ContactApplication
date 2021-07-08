package com.contact.contactapplication

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.text.TextUtils



object ContactRepositary {

    fun getRequestData(
        successHandler: (ArrayList<ContactPojo>) -> Unit, c:
        Context, failureHandler: (String) -> Unit, onFaliureHandler:
            (Throwable) -> Unit
    ) {

        var arrListContact = ArrayList<ContactPojo>()
        val cr: ContentResolver = c.contentResolver
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )



        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                var contact=ContactPojo()
                val id: String = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name: String = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )

                if(!TextUtils.isEmpty(name))
                    contact.setName(name)
                if (cur.getInt(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    ) > 0
                ) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {
                        val phoneNo: String = pCur.getString(
                            pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                        if(!TextUtils.isEmpty(phoneNo))
                            contact.setNumber(phoneNo)

                    }
                    pCur.close()
                }

                arrListContact.add(contact);
            }
        }
        cur?.close()

        successHandler(arrListContact)


    }
}