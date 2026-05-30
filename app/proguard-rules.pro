# Retrofit Library ကုဒ်များ ပျက်စီးမသွားစေရန် ထိန်းသိမ်းခြင်း
-keep attributes *Annotation*,Signature,InnerClasses
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# OpenAI API ရဲ့ Response ဒေတာ Model များ (Gson) ကို ပုံစံမပျက် သိမ်းဆည်းရန်
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Network ဒေတာတွေကို ဖတ်တဲ့အခါ Error မတက်စေရန်
-keep class com.ai.zyntraai.** { *; }
