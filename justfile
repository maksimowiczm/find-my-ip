default:
    @just --list

# $KTFMT_JAR - path to the ktfmt jar file
format:
    @find . -type f \( -name "*.kt" -o -name "*.kts" \) -not -path "*/build/*" | xargs java -jar $KTFMT_JAR --kotlinlang-style

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
