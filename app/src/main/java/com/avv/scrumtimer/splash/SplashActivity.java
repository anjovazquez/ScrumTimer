package com.avv.scrumtimer.splash;

import android.content.Intent;

import com.avv.scrumtimer.R;
import com.avv.scrumtimer.MainActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by angelvazquez on 15/5/16.
 */
public class SplashActivity extends AwesomeSplash {

    public static final String TEST_LOGO = "M 256.00,0.00\r\n           C 256.00,0.00 256.00,256.00 256.00,256.00\r\n             256.00,256.00 0.00,256.00 0.00,256.00\r\n             0.00,256.00 0.00,0.00 0.00,0.00\r\n             0.00,0.00 256.00,0.00 256.00,0.00 Z\r\n           M 36.74,6.15\r\n           C 35.25,9.44 35.40,19.95 38.31,22.40\r\n             42.29,25.75 47.29,21.61 49.29,28.04\r\n             49.29,28.04 51.27,37.44 51.27,37.44\r\n             51.27,37.44 55.42,50.00 55.42,50.00\r\n             61.22,65.34 70.90,80.49 81.44,93.00\r\n             87.51,100.21 93.88,107.25 101.00,113.42\r\n             104.02,116.04 111.80,122.41 112.89,126.01\r\n             114.03,129.81 111.35,132.45 108.96,135.00\r\n             108.96,135.00 91.09,152.00 91.09,152.00\r\n             77.33,166.77 63.55,185.13 56.05,204.00\r\n             56.05,204.00 49.16,228.85 49.16,228.85\r\n             47.10,233.75 41.93,230.76 38.43,233.17\r\n             35.43,235.24 35.96,240.74 36.00,244.00\r\n             36.12,252.67 38.01,252.98 46.00,253.05\r\n             46.00,253.05 179.00,253.05 179.00,253.05\r\n             179.00,253.05 214.98,253.05 214.98,253.05\r\n             221.02,252.26 220.06,245.56 220.00,241.00\r\n             219.96,238.27 220.10,234.92 217.57,233.17\r\n             214.09,230.78 208.82,233.68 206.58,228.85\r\n             206.49,228.66 203.45,212.71 200.83,206.00\r\n             194.99,191.02 182.68,171.87 171.93,160.00\r\n             163.73,150.96 161.39,148.13 152.00,139.85\r\n             149.06,137.25 143.49,132.99 142.86,129.00\r\n             142.23,124.99 145.42,122.16 148.04,119.72\r\n             148.04,119.72 165.72,103.00 165.72,103.00\r\n             179.90,86.91 191.70,72.11 199.94,52.00\r\n             199.94,52.00 204.67,37.59 204.67,37.59\r\n             204.67,37.59 206.73,27.15 206.73,27.15\r\n             208.88,22.54 213.38,25.04 216.85,22.98\r\n             220.61,20.76 220.05,15.76 220.00,12.00\r\n             219.88,3.33 217.99,3.02 210.00,3.00\r\n             210.00,3.00 95.00,3.00 95.00,3.00\r\n             95.00,3.00 59.00,3.00 59.00,3.00\r\n             53.58,3.00 44.13,2.46 39.23,3.60\r\n             37.86,4.59 37.56,4.32 36.74,6.15 Z\r\n           M 200.00,24.00\r\n           C 199.07,35.37 193.57,51.10 188.00,61.00\r\n             186.10,64.37 183.82,68.95 180.82,71.35\r\n             176.12,75.10 163.11,77.45 157.00,78.38\r\n             136.33,81.67 119.55,81.15 98.96,78.38\r\n             93.58,77.63 79.21,74.57 75.18,71.44\r\n             72.31,69.21 68.53,62.37 66.78,59.00\r\n             60.98,47.83 57.83,36.37 56.00,24.00\r\n             56.00,24.00 200.00,24.00 200.00,24.00 Z\r\n           M 123.24,148.96\r\n           C 123.24,148.96 127.00,177.00 127.00,177.00\r\n             127.00,177.00 113.00,189.57 113.00,189.57\r\n             113.00,189.57 77.00,216.00 77.00,216.00\r\n             77.00,216.00 56.00,229.00 56.00,229.00\r\n             59.69,217.55 59.88,212.53 65.44,200.00\r\n             69.91,189.92 80.28,175.49 87.44,167.00\r\n             87.44,167.00 101.01,152.17 101.01,152.17\r\n             108.20,145.51 115.30,140.81 120.00,132.00\r\n             120.00,132.00 123.24,148.96 123.24,148.96 Z\r\n           M 140.34,138.72\r\n           C 140.34,138.72 156.96,154.01 156.96,154.01\r\n             168.31,165.59 182.38,183.47 189.63,198.00\r\n             194.72,208.19 197.67,218.92 200.00,230.00\r\n             183.85,226.51 160.50,210.68 149.04,198.99\r\n             142.26,192.07 136.35,182.69 129.00,177.00\r\n             129.00,177.00 135.00,132.00 135.00,132.00\r\n             135.00,132.00 140.34,138.72 140.34,138.72 Z";

    @Override
    public void initSplash(ConfigSplash configSplash) {

        /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1200); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_TOP); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.ic_launcher); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.RotateIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)
        configSplash.setOriginalHeight(256); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(256); //in relation to your svg (path) resource

        //Customize Path
        /**configSplash.setPathSplash(TEST_LOGO); //set path String
        configSplash.setOriginalHeight(256); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(256); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(1500);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.white); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(1500);
        configSplash.setPathSplashFillColor(R.color.colorAccent);**/
        //path object filling color


        //Customize Title
        configSplash.setTitleSplash("Scrum Timer");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.BounceInDown);
        //configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {

        //transit to another activity here
        //or do whatever you want

        startActivity(new Intent(this, MainActivity.class));
    }
}
