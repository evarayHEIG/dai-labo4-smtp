## But du programme

## instructions du fonctionnement (comment le lancer)

## Diagramme de classe


```mermaid
---
title: SMTP
---
classDiagram
    
    class FileManager {
        +String[] getVictims()
        +int getNbGroup()
    }
    
    class Client {
        +void main(String[] args)
        -void run()
    }
    
    class MailGroup {
        #createGroup(int groupSize)
    }
    
    class MailContent {
        -String mailFrom(String sender)
        -String mailTo(String victim)
        +String createMailSMTP()
    }


```