package pimpbot.nestedrecyclerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val frag  = PrimaryRecyclerViewFragment()
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
            .add(R.id.simple_fragment_container,  frag)
                .commit()

    }
}
