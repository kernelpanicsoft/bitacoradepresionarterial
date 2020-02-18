package com.ga.kps.bitacoradepresionarterial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        title = getString(R.string.app_name)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        /*
        auxButton.setOnClickListener {
            val nav = Intent(this, UserListActivity::class.java)
            startActivity(nav)
        }
*/



        setupViewPager(ViewPagerPrincipal)
        TabLayoutPrincipal.setupWithViewPager(ViewPagerPrincipal)

        ViewPagerPrincipal.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if(position == 0){
                  //  addShotFAB.show()
                }else{
                  //  addShotFAB.hide()
                }


            }
        })

        addShotFAB.setOnClickListener {
            val nav = Intent(this, AddShotActivity::class.java)
            startActivity(nav)
        }



        nav_view.setNavigationItemSelectedListener { menuItem ->
            drawer_layout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_account ->{
                    val navProfile = Intent(this, UserDetailsActivity::class.java)
                    startActivity(navProfile)
                }
                R.id.nav_change_user ->{
                    val navChangeUser = Intent(this, UserListActivity::class.java)
                    startActivity(navChangeUser)
                }
                R.id.nav_settings ->{
                    
                }

            }
            true
        }



    }


    private fun setupViewPager(pager: ViewPager){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ShotsFragment(),"Tomas")
        adapter.addFragment(StatsFragment(),"Estadisticas")
        adapter.addFragment(RemindersFragment(),"Recordatorios")

        pager.adapter = adapter


    }

    private inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()


        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentTitleList.size
        }

        fun addFragment(fragment: Fragment, title: String){
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence{
            return mFragmentTitleList[position]
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean{
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                drawer_layout.openDrawer(GravityCompat.START)
            }
            R.id.itemExport ->{

            }

        }
        return super.onOptionsItemSelected(item)
    }
}
