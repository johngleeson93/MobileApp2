package ie.wit.landmark.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.vision.face.Landmark
import ie.wit.landmark.adapters.LandmarkAdapter
import ie.wit.landmark.main.LandmarkApp
import landmark.R
import landmark.databinding.ActivityReportBinding

class Report : AppCompatActivity() {

    lateinit var app: LandmarkApp
    lateinit var reportLayout : ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportLayout = ActivityReportBinding.inflate(layoutInflater)
        setContentView(reportLayout.root)

        app = this.application as LandmarkApp
        reportLayout.recyclerView.layoutManager = LinearLayoutManager(this)
        reportLayout.recyclerView.adapter = LandmarkAdapter(app.landmarksStore.findAll())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_report, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_landmark -> { startActivity(
                Intent(this,
                Landmark::class.java)
            )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}