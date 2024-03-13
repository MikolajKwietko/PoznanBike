# Aplikacja PoznanBikeAPI

Aplikacja wykorzystująca API do wyświetlania stacji rowerowych w Poznaniu i dostępności rowerów w nich.

Aplikacja pozwala na rejestrację użytkownika, zalogowanie się kontem do aplikacji. Aplikacja posiada interaktywną mapę budynku z pokojami objętymi obserwacją. Pozwala na obserwację obrazu z obserwowanych pokojów oraz informuje użytkowników o zaistniałych zagrożeniach.

## Instalacja i konfiguracja

Należy pobrać projekt jako ZIP i uruchomić go w Android Studio.

### Zależności

Wykorzystane biblioteki w projekcie:
- [Retrofit](https://square.github.io/retrofit/)
- [Androidx](https://developer.android.com/jetpack/androidx)
- [Room](https://developer.android.com/training/data-storage/room)

Instrukcje instalacji bibliotek są zamieszczone w zamieszczonych linkach.

Projekt wykorzystuje [SafeArgs](https://developer.android.com/guide/navigation/navigation-pass-data#Safe-args) do przekazywania danych między fragmentami. W celu dodania do projektu należy w pliku `build.gradle` na poziomie projektu dodać `classpath` do najnowszej wersji [SafeArgs](https://developer.android.com/guide/navigation/navigation-pass-data#Safe-args):

```Kotlin
buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
}
```

***
