#-keepclasseswithmembers class * {
#    public <init>(android.content.Context);
#}

-keep class com.it.calendar.calendarview.MonthView {
    public <init>(android.content.Context);
}
-keep class com.it.calendar.calendarview.WeekBar {
    public <init>(android.content.Context);
}
-keep class com.it.calendar.calendarview.WeekView {
    public <init>(android.content.Context);
}
-keep class com.it.calendar.calendarview.YearView {
    public <init>(android.content.Context);
}