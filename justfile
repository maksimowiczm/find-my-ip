default:
    @just --list

# $KTFMT_JAR - path to the ktfmt jar file
format:
    @find . -type f \( -name "*.kt" -o -name "*.kts" \) -not -path "*/build/*" | xargs java -jar $KTFMT_JAR --kotlinlang-style

screenshot output="screenshots/":
    @adb shell rm -fr /sdcard/Pictures/com.maksimowiczm.findmyip
    @./gradlew :composeApp:connectedAndroidTest
    @mkdir -p {{ output }}
    @adb shell find /sdcard/Pictures/com.maksimowiczm.findmyip -iname "*.png" | while read line; do adb pull "$line" {{ output }}; done

release-opensource:
    @./gradlew --no-daemon --no-build-cache clean
    @./gradlew --no-daemon --no-build-cache assembleRelease
    @zipalign -f -p -v 4 \
      composeApp/build/outputs/apk/release/composeApp-release-unsigned.apk \
      composeApp/build/outputs/apk/release/aligned-unsigned.apk
    @apksigner sign \
      --alignment-preserved \
      --out ./release-opensource-signed.apk \
      --ks keystore \
      --ks-key-alias secret_key \
      --ks-pass stdin \
      --key-pass stdin \
      composeApp/build/outputs/apk/release/aligned-unsigned.apk
