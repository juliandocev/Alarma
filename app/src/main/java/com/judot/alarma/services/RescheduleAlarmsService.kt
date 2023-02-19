package com.judot.alarma.services

import androidx.lifecycle.LifecycleService

/** We use the service to create the alarm. Normally the scheduled alarm
 * will be lost on turn off or restarting the phone.  The service  it is the way
 * to call a function after a boot. So we save the alarm and if the phone
 * reboots  it schedules the alarm again */
class RescheduleAlarmsService: LifecycleService(){
}