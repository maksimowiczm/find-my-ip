default:
    @just --list

# $KTFMT_JAR - path to the ktfmt jar file
format:
    @find . -type f \( -name "*.kt" -o -name "*.kts" \) -not -path "*/build/*" | xargs java -jar $KTFMT_JAR --kotlinlang-style

screenshot:
    @adb shell rm -fr /sdcard/Pictures/com.maksimowiczm.findmyip
    @./gradlew :composeApp:connectedAndroidTest
    @adb shell find /sdcard/Pictures/com.maksimowiczm.findmyip -iname "*.png" | while read line; do adb pull "$line" metadata/en-US/images/phoneScreenshots/; done

release-opensource:
    @./gradlew clean
    @./gradlew --no-daemon assembleOpenSourceIpifyRelease
    @zipalign -f -p -v 4 \
      app/build/outputs/apk/openSourceIpify/release/app-openSource-ipify-release-unsigned.apk \
      app/build/outputs/apk/openSourceIpify/release/aligned.apk
    @apksigner sign \
      --alignment-preserved \
      --out ./release-opensource-signed.apk \
      --ks keystore \
      --ks-key-alias secret_key \
      --ks-pass stdin \
      --key-pass stdin app/build/outputs/apk/openSourceIpify/release/aligned.apk
