package info.mayankag.parlorbeacon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class SimpleWidgetProvider extends AppWidgetProvider {

    private static PendingIntent actionPendingIntent(Context context) {
        Intent intent = new Intent(context, AboutDev.class);
        intent.setAction("LAUNCH_ACTIVITY");
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent actionPendingIntent1(Context context) {
        Intent intent = new Intent(context, UpcomingFeatures.class);
        intent.setAction("LAUNCH_ACTIVITY");
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, SimpleWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.simple_widget);
        remoteViews.setOnClickPendingIntent(R.id.aboutdev, actionPendingIntent(context));
        remoteViews.setOnClickPendingIntent(R.id.ufeatures, actionPendingIntent1(context));
        pushWidgetUpdate(context, remoteViews);

    }

}
