default:
    @just --list

# KTLINT_COMPOSE_JAR - path to the ktlint-compose jar file
format:
    @ktlint -R $KTLINT_COMPOSE_JAR --editorconfig="./.editorconfig" --format

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
