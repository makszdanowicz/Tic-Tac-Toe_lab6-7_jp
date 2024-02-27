# Treść programowa

1. Rozpraszanie obliczeń poprzez wykorzystanie gniazd TCP/IP.
2. Rozpraszanie obliczeń poprzez zdalne wywoływanie procedur.
```
W celu wygospodarowania dodatkowych zajęć na ewentualne poprawy laboratoria 6 i 7 są połączone.
Należy wykonać jedną aplikację (osobny klient i serwer) realizującą komunikację zarówno poprzez sockety TCP/IP jaki i zdalne wywoływanie procedur (”Remote Method Invocation” - RMI).

```
# Cel zajęć

Celem zajęć jest implementacja gry “kółko i krzyżyk” (”*tick-tack-toe*”). Gra powinna działać w architekturze klient-serwer.

# Projekt

### Kluczowe funkcjonalności

1. **Hybrydowa komunikacja sieciowa** - użycie zarówno komunikacji poprzez RMI jak i gniazd TCP/IP.
2. **Mechanika gry** - implementacja silnika zarządzającego mechaniką gry w kółko i krzyżyk

### Obie grupy

- Należy wykorzystać mechanizm RMI w celu nawiązania połączenia między dwoma graczami oraz obsługę mechaniki gry (np. parowanie graczy, inicjalizowanie stanu gry, wymiana ruchów, walidacja I/O).
- Mechanika gry powinna być zaimplementowana w aplikacji serwera.
- Należy przemyśleć argumenty pobierane przez aplikacje JAR serwer/klient.
- Serwer powinen obsługiwać więcej niż jednego gracza w tym samym momencie (np. poprzez realizację gier w dedykowanych pokojach lub tworzenie tokenów poszczególnych sesji wymienianych przez graczy).
- Serwer powinen obliczać statystyki graczy w danej sesji - ilość wygranych, remisów oraz porażek.
- Aplikacja serwera powinna logować w konsoli kluczowe dane (parowanie użytkowników, błędy, koniec gry, itp).

## Grupa A

- Implementacja funkcjonalności chatu pomiędzy użytkownikami z wykorzystaniem protokołu TCP/IP. Gniazda powinny być otworzone pomiędzy grającymi użytkownikami (bez udziału serwera).

## Grupa B

- Implementacja “*trybu obserwatora*”. Polega on na tym, że niezależny klient może podłączyć się do istniejącej sesji i mieć możliwość obserwacji trwającej rozgrywki. Tryb należy zaimplementować wykorzystując protokół TCP/IP.

## Credits
[Strona Pana Doktora Tomasza Kozłowskiego](https://khozzy.notion.site/Laboratorium-6-7-Gniazda-TCP-IP-RMI-1521c6a4fee34bd8ad18448d23d8779a)
