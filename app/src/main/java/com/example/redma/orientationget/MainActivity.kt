package com.example.redma.orientationget

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), SensorEventListener {
    private val matrixSize = 16
    //センサーの値（配列）
    private var mgValues = FloatArray(3)
    private  var acValues = FloatArray(3)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val inR = FloatArray(matrixSize)
        val out = FloatArray(matrixSize)
        val I = FloatArray(matrixSize)
        val orValues = FloatArray(3)

        if(event == null) return //eventがからでないことを確認
        //センサーのタイ鵜を確認
        when(event.sensor.type){
            //各センサーの値を取得し変数へ保存
            Sensor.TYPE_ACCELEROMETER -> acValues = event.values.clone()    //加速度センサー
            Sensor.TYPE_MAGNETIC_FIELD -> mgValues = event.values.clone()  //磁気センサー
        }

        //加速度センサーと磁気センサーから回転行(inR),回転列(I)を作成
        SensorManager.getRotationMatrix(inR, I, acValues, mgValues)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public override fun onResume() {
        super.onResume()
        //センサーマネージャーインスタンスを取得⇒戻り値がObject型のためSensorManager型にキャスト
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //加速センサーオブジェクトを取得
        val acceleremoter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        //磁気センサーオブジェクトを取得
        val magField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        //加速センサーをリスナーに登録⇒これをしないとAndroidがアプリにセンサー情報を連携してくれない
        sensorManager.registerListener(this,acceleremoter, SensorManager.SENSOR_DELAY_NORMAL)
        //磁気センサーもリスナーに登録
        sensorManager.registerListener(this,magField,SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        //センサーマネージャーのインスタンスを取得⇒戻り値がObject型のためSensorManager型にキャスト
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //リスナーを解除
        sensorManager.unregisterListener(this)
    }


}
