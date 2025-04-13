# 🚀 Prosta Aplikacja Backlogu w Java Swing 🚀

Ta aplikacja to **proste i intuicyjne** narzędzie do zarządzania zadaniami w stylu backlogu, inspirowane popularnymi tablicami Kanban. Została stworzona w całości w języku **Java** z wykorzystaniem biblioteki **Swing** do budowy przejrzystego interfejsu graficznego. Aplikacja umożliwia **wizualne śledzenie postępu prac** nad zadaniami poprzez ich łatwe przesuwanie między różnymi panelami, które reprezentują poszczególne etapy procesu.

## ✨ Kluczowe Funkcjonalności ✨

* **Elastyczne Panele Zadań:**
    * Domyślnie zawiera panele: **Milestones**, **Todo**, **To correct**, **In Progress** i **Done**.
    * **Możliwość dodawania i usuwania własnych paneli** w celu dostosowania do specyficznego przepływu pracy.
* **Łatwe Dodawanie Zadań:**
    * Prosty interfejs do wprowadzania **opisów nowych zadań**, które są początkowo dodawane do pierwszego panelu.
* **Personalizacja Kolorów:**
    * Opcja **wyboru globalnego koloru** dla nowo tworzonych zadań, co ułatwia ich wizualną identyfikację.
    * **Możliwość zmiany koloru poszczególnych zadań** poprzez dwukrotne kliknięcie.
* **Sprawne Zarządzanie Zadaniami:**
    * **Usuwanie (ukrywanie) zadań:** Opcja tymczasowego usunięcia zaznaczonego zadania z widoku.
    * **Przenoszenie między panelami:** Intuicyjne przenoszenie zadań między sąsiednimi panelami za pomocą wygodnych przycisków **"<"** i **">"** umieszczonych u dołu każdego panelu.
* **Dynamiczne Zarządzanie Panelami:**
    * **Dodawanie nowych paneli:** Możliwość rozszerzenia tablicy backlogu o kolejne etapy pracy.
    * **Usuwanie istniejących paneli:** Proste usuwanie niepotrzebnych paneli (zabezpieczenie przed usunięciem ostatniego panelu).

## 🛠️ Jak Uruchomić Aplikację 🛠️

1.  **Wymagania:** Upewnij się, że masz zainstalowane **Java Development Kit (JDK)** na swoim systemie.
2.  **Kompilacja:** Skompiluj pliki źródłowe Java. Możesz to zrobić za pomocą kompilatora `javac`:
    ```bash
    javac BacklogApp.java TaskPanel.java Task.java TaskCellRenderer.java
    ```
    (Zakładając, że klasy wewnętrzne zostały wyodrębnione do oddzielnych plików)
3.  **Uruchomienie:** Uruchom skompilowaną aplikację, wykonując główną klasę `BacklogApp`:
    ```bash
    java BacklogApp
    ```

## ⚙️ Technologie Użyte ⚙️

* **Java**: Główny język programowania.
* **Swing**: Biblioteka do tworzenia graficznego interfejsu użytkownika.
* **AWT**: Abstrakcyjny zestaw narzędzi okienkowych, część standardowej biblioteki Javy, używany przez Swing.
