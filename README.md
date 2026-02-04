# IR Light Remote

An Android-based Infrared (IR) remote application specifically designed for the Mondo Ceiling LED
Lamp. This project was created because no existing universal remote database or app supported the
specific NEC-style codes required for this hardware.

| Function        | Command (Hex) | Full Code (Profile A) | Full Code (Profile B) |
|:----------------|:--------------|:----------------------|:----------------------|
| Power           | 0xE9          | 0xA0B7E9              | 0xAD09E9              |
| Sleep Mode      | 0xF5          | 0xA0B7F5              | 0xAD09F5              |
| Balance Mode    | 0xAC          | 0xA0B7AC              | 0xAD09AC              |
| Brightness Up   | 0xAB          | 0xA0B7AB              | 0xAD09AB              |
| Brightness Down | 0xBC          | 0xA0B7BC              | 0xAD09BC              |
| Balance Up      | 0xEF          | 0xA0B7EF              | 0xAD09EF              |
| Balance Down    | 0xEB          | 0xA0B7EB              | 0xAD09EB              |
| Memory          | 0xAF          | 0xA0B7AF              | 0xAD09AF              |
| Timer           | 0xFA          | 0xA0B7FA              | 0xAD09FA              |

# Technical Requirements

- Hardware: Android device with an integrated IR Blaster.
- OS: Android 7.0 (API 24) or higher.

# How to Build

To generate a release APK, run:

`./gradlew assembleRelease`