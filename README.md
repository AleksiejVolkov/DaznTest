# DaznTest
Application for dazn interview

## Task Description

There should be two tabs in the app, prefereably put in a form of a **bottom navigation**. The first one
should present a list of events and the second one a schedule (EPG).

#### First Screen

Events screen should present list of events available to be watched. When any item is tapped, the apprioriate video is played on Playback Screen. Use the standard APIs to present a list of items
provided by our API. Assume that the list can get fairly long (e.g up to 100 items). Events should be **ordered** by date in ascending order.

#### Second Screen

Schedule screen, with a schedule for tomorrow only. It should **auto-refresh** every 30 seconds. Assume that the list can get fairly long (e.g up to 100 items) Events should be **ordered** by date in ascending order.

*Extra points will be awarded for updating the state of the list without loosing its scroll position and without flickering.*

#### Third Screen

Additionally, there should be a Playback Screen, playing a video in the default player. Use wchichever player you prefer. The aim is to be able to play the video that is provided in selected event.

### Aditional notes

Consider writing at least a few Unit Tests for your implementation. These tests could cover any
relevant part of your application.

What are we looking for in the solution (don’t stress if you feel you didn’t get all the below points
right- we have an interview where we can just talk about the stuff you didn’t have time to address):
- Clean code
- Approach to architecture in a small app
- Object-oriented programming
- SOLID
- Language knowledge and experience
- Basic memory management
- Unit tests
- Testing practices
- QA practices
- REST APIs usage practices
- Handling Activity lifecycle

## My Solution

#### First Screen

![image](https://github.com/user-attachments/assets/13e2ff58-c0d4-4f1c-97e3-a754a4b6261c)

#### Second Screen

![image](https://github.com/user-attachments/assets/a1c925ce-353f-48f8-96c2-27d705e702c7)

#### Video Screen

![image](https://github.com/user-attachments/assets/ca0a269c-d85f-44ab-b7ab-6d5535a6ba15)

### Summary

I've implemented all goals from the objective with all extra tasks. In addition I've added:

- Splash screen
- Media notification (and service) for playing video in BG as well
- Add some runtime shaders for stunning look (loading, image placeholder)
- Support retry if network faild (and display network error)
- Auto reload on network state change (if no data was loaded)
- Dark mode, animations
- In addition to Unit tests added Preview for some **Composable** elements to show how it works

Hope it would be quite enough. Some extra screenshots from mid-states that may be not noticable during normal app usign due to fast and stable internet connection:

![image](https://github.com/user-attachments/assets/419672d6-a00d-4354-9db8-a9d46ee4c6bc)

![image](https://github.com/user-attachments/assets/3b7fadf9-8c63-4168-aec0-8ed57e83ef0f)

![image](https://github.com/user-attachments/assets/6d08af5c-bcdb-40f7-ae07-f27144d0417a)











