package com.example.mylib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mylib.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.notificationman.library.NotificationMan
import com.notificationman.library.model.NotificationTypes
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis


class MainActivity : AppCompatActivity() {

    companion object {
        private const val THUMBNAIL_URL =
            "https://storage.googleapis.com/gweb-uniblog-publish-prod/images/Android_robot.max-500x500.png"
    }


    private lateinit var binding: ActivityMainBinding
    private var firebaseDataBase: FirebaseDatabase?= null
    private var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomView)
        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)


        var tanggal = currentTimeMillis()
        var noinduk = intent.getStringExtra("no_induk").toString()

        firebaseDataBase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDataBase?.getReference("meminjam")

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    if (ds.child("no_induk").value.toString().equals(noinduk)){
                        val tanggal_pengembalian = ds.child("tanggal_pengembalian").value.toString().trim().toLongOrNull()
                        var timeInterval = (tanggal_pengembalian!!.toLong() - tanggal - 100)

                        // Toast.makeText(applicationContext, "${timeInterval}", Toast.LENGTH_SHORT).show()
//                        Log.e("1","size:${tanggal} ")
//                        Log.e("2","size:${tanggal_pengembalian} ")
//                        Log.e("3","size:${timeInterval} ")

                        val classPath = "com.example.mylib.MainActivity"
                        fireNotificationMan(
                            classPath = classPath,
                            title = "Pengembalian Buku",
                            desc = "Sekarang",
                            timeInterval = timeInterval
                        )

//                        ds.child("tanggal_pengembalian").value.toString())
//                        Toast.makeText(applicationContext, "${ds.child("tanggal_pengembalian").value.toString()}", Toast.LENGTH_SHORT).show()
                    }

                }

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun fireNotificationMan(
        classPath: String,
        title: String?,
        desc: String?,
        timeInterval: Long?,
    ) {
        NotificationCompat.Builder(applicationContext)
        NotificationMan
            .Builder(this, classPath)
            .setTitle(title)
            .setDescription(desc)
            .setThumbnailUrl(THUMBNAIL_URL)
            .setTimeInterval(timeInterval)
            .setNotificationType(NotificationCompat.PRIORITY_MAX)
            .fire()
    }

    }
//        setSupportActionBar(toolbar)
//
//        val titleNotification = getString(R.string.notification_title)
//        collapsing_toolbar_l.title = titleNotification
//
//        done_fab.setOnClickListener {
//            val customCalendar = Calendar.getInstance()
//            customCalendar.set(
//                date_p.year, date_p.month, date_p.dayOfMonth, time_p.hour, time_p.minute, 0
//            )
//            val customTime = customCalendar.timeInMillis
//            val currentTime = currentTimeMillis()
//            if (customTime > currentTime) {
//                val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
//                val delay = customTime - currentTime
//                scheduleNotification(delay, data)
//
//                val titleNotificationSchedule = getString(R.string.notification_schedule_title)
//                val patternNotificationSchedule = getString(R.string.notification_schedule_pattern)
//                make(
//                    coordinator_l,
//                    titleNotificationSchedule + SimpleDateFormat(
//                        patternNotificationSchedule, getDefault()
//                    ).format(customCalendar.time).toString(),
//                    LENGTH_LONG
//                ).show()
//            } else {
//                val errorNotificationSchedule = getString(R.string.notification_schedule_error)
//                make(coordinator_l, errorNotificationSchedule, LENGTH_LONG).show()
//            }
//        }
//    }
//
//    private fun scheduleNotification(delay: Long, data: Data) {
//        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
//            .setInitialDelay(delay, MILLISECONDS).setInputData(data).build()
//
//        val instanceWorkManager = WorkManager.getInstance(this)
//        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, REPLACE, notificationWork).enqueue()
//    }
