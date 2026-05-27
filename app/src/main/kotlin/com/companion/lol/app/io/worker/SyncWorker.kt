package com.companion.lol.app.io.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.companion.lol.app.AppConst
import com.companion.lol.data.usecase.RefreshChampionsUseCase
import com.companion.lol.data.util.getOrPropagate
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.time.Duration
import kotlin.time.toJavaDuration
import timber.log.Timber

@HiltWorker
class SyncWorker
@AssistedInject
constructor(
  @Assisted context: Context,
  @Assisted workerParams: WorkerParameters,
  private val refreshChampions: Lazy<RefreshChampionsUseCase>,
) : CoroutineWorker(context, workerParams) {
  companion object {

    private const val PERIODIC_WORK_NAME = "periodic_sync"

    fun schedulePeriodicSync(
      context: Context,
      repeatInterval: Duration = AppConst.syncRepeatDuration,
      startAfterInterval: Boolean = true,
    ) {
      val workManager = WorkManager.getInstance(context)

      /*// we use the existence of the periodic sync work to
      // check if we need the initial sync (first time)
      val workInfos = workManager.getWorkInfosForUniqueWork(PERIODIC_WORK_NAME).get()

      val hasExistingWork =
        workInfos.any { info ->
          info.state == WorkInfo.State.ENQUEUED ||
            info.state == WorkInfo.State.RUNNING ||
            info.state == WorkInfo.State.BLOCKED
        }

      if (!hasExistingWork) {
        // 1) Immediate sync (runs ASAP once)
        workManager.enqueue(
          OneTimeWorkRequestBuilder<SyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(
              Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()
        )
      }*/
      val duration = repeatInterval.toJavaDuration()
      val periodicSync =
        PeriodicWorkRequestBuilder<SyncWorker>(duration)
          .setConstraints(
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
          )
          .apply {
            if (startAfterInterval) {
              setInitialDelay(duration)
            }
          }
          .build()

      workManager.enqueueUniquePeriodicWork(
        PERIODIC_WORK_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        periodicSync,
      )
    }

    fun cancelPeriodicSync(context: Context) {
      WorkManager.getInstance(context).cancelUniqueWork(PERIODIC_WORK_NAME)
    }
  }

  override suspend fun doWork(): Result {
    refreshChampions.get().refresh().getOrPropagate {
      Timber.e(it)
      return@doWork Result.retry()
    }

    return Result.success()
  }
}
