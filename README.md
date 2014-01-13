ASWProject
==========

TODO
----

- ~~Registration/Login (jsp + form html?)~~
- ~~User search (WS + applet)~~
- ~~Sharing messages (WS + applet)~~
- ~~Following users (WS + applet)~~
- ~~User stream (WS + applet)~~
- ~~Users profiles: stats + messages (WS + applet)~~
- Check xml produced
- Clean code
- JavaDoc
- ~~html documentation page~~ (this README, converted to html)



Titolo
======
Share2Me

Autori
======
Alessandro Zoffoli - 657273 - alessandro.zoffoli2@studio.unibo.it

Mattia Baldani - 681182 - mattia.baldani@studio.unibo.it

DESCRIZIONE DEL SERVIZIO OFFERTO DAL SITO
=========================================
WebApp social, ispirata a Twitter, che permette ad ogni utente di cercare e seguire altri utenti, e di condividere uno stato (una qualsiasi frase) agli utenti da cui è seguito.

UTILIZZO DEL SITO
=================
Tipologie di utenti:

1. Utenti non registrati
  * Registrazione
  * Ricerca utenti e visualizzazione stati condivisi
  
2. Utenti registrati
  * Login/Logout
  * Condivisione stato
  * Aggiunta/Rimozione di followers
  * Ricerca utenti e visualizzazione stati condivisi
  
3. Utenti amministratori
  * Rimozione di utenti e stati non appropriati
  * Tutto quello previsto per gli utenti registrati

REALIZZAZIONE DEL SITO - Sommario
=================================
Computazione lato client
------------------------
Consiste in diverse RIA eseguite come applet, incorporate in pagine web. In particolare:

1. PostTweetApplet, che consente di scrivere e publicare un nuovo stato. 
2. UserSearchApplet, che consente all'utente di ricercare altri utenti.
3. TweetListApplet, che consente all'utente di visualizzare la lista di stati condivisi dai suoi following o dal singolo utente, a seconda del contesto.

Computazione lato server
------------------------
È compresa nelle seguenti JSP:

1. index.jsp, che è la pagina principale dell'applicazione. Se l'utente è loggato, mostra all'utente gli ultimi stati degli utenti che segue, oltre che a permettere l'aggiunta di un nuovo post. Se l'utente non è loggato, propone i form di registrazione e login, e mostra all'utente gli ultimi post provenienti dalla totalità degli utenti registrati.
2. login.jsp, che verifica le credenziali dell'utente e lo redirige alla pagina principale.
3. logout.jsp, che slogga l'utente e  lo redirige alla pagina principale.
4. profile.jsp, che mostra il profilo (tutti gli stati postati) dell'utente selezionato.
5. search.jsp, che permette all'utente di cercare altri utenti, di aggiungerli ai suoi following e di visualizzarne il profilo.

Le servlet utilizzate sono:

1. RegistrationServlet, che provvede alla registrazione di un nuovo utente (che puó anche inserire una foto del profilo).
2. DownloadFileServlet, che provvede a restituire la foto del profilo a partire da uno username.
3. TweetListService (XML over HTTP), che racchiude tutte le operazioni legate alla gestione degli stati condivisi.
4. UserListService (XML over HTTP), che racchiude tutte le operazione legate alla gestione degli utenti.

Informazioni memorizzate sul server e scambiate sulla rete
----------------------------------------------------------

Le informazioni memorizzate sul server sono quelle realtive a:

1. gli utenti registrati (users.xml).
2. gli stati condivisi (tweets.xml).

Le informazioni scambiate sono principalmente suddivisibili in:

1. xml presente nelle request, generalmente formato da un nodo radice che contiene l'operazione richiesta e opzionalmente altri nodi, contenenti parametri da elaborare (es: nella ricerca degli utenti, la stringa con cui filtrare al lista degli utenti).
2. xml presente nelle response, generalmente formato da una lista di elementi.


REALIZZAZIONE DEL SITO - Tecnologie
===================================

Computazione lato client
------------------------

1. PostTweetApplet. È una semplice applet che utilizza HTTPClient e ManageXML per postare un nuovo tweet.
2. UserSearchApplet. È una applet piú complessa, formata da un campo di ricerca e un bottone, e una lista. Utilizza HTTPClient e ManageXML per richiedere la lista degli utenti che matchano la stringa inserita, e che vengono riportati nella lista. Tale applet utilizza un ListCellRenderer (UserListCellRenderer) per visualizzare in maniera piú gradevole i dati e una ImageCache per ottimizzare il download e la visualizzazione delle immagini del profilo dei vari utenti.
3. TweetListApplet. È una applet, la computazione avviene nel seguente modo:
 * Alla prima apertura, gli stati vengono recuperati con una richiesta.
 * Successivamente, la lista viene aggiornata attraverso un comet service.
  
 La scelta di implementare le funzionalità principali come applet deriva principalmente dalla volontà di avere una esperienza utente uniforme nelle varie pagine del sito. 
L'unica funzionalità che realmente richiedeva di essere implementata come applet è TweetListApplet, in quanto comunica col server in modo interattivo.

Computazione lato server
------------------------
1. index.jsp, che è la pagina principale dell'applicazione. Se l'utente è loggato, mostra all'utente gli ultimi stati degli utenti che segue, oltre che a permettere l'aggiunta di un nuovo post. Se l'utente non è loggato, propone i form di registrazione e login, e mostra all'utente gli ultimi post provenienti dalla totalità degli utenti registrati.
2. login.jsp, che verifica le credenziali dell'utente e lo redirige alla pagina principale.
3. logout.jsp, che slogga l'utente e  lo redirige alla pagina principale.
4. profile.jsp, che mostra il profilo (tutti gli stati postati) dell'utente selezionato.
5. search.jsp, che permette all'utente di cercare altri utenti, di aggiungerli ai suoi following e di visualizzarne il profilo.

Le servlet utilizzate sono:

1. RegistrationServlet, che riceve i dati dal form di registrazione (testuali e immagine) e provvede a salvarli (db xml e directory contenente tutte le immagini).
2. DownloadFileServlet, che prende il nome dello username dai parametri della richiesta e restituisce l'immagine del profilo corrispondente.
3. TweetListService, che gestisce diversi tipi di operazioni, correlate agli stati:
 * getTweets, che ritorna la lista dei tweets da visualizzare
 * postTweet, che permette l'inserimento
 * waitForUpdate, che innesca il meccanismo del comet service.

4. UserListService, che gestisce diversi tipi di operazioni, correlate agli utenti:
 * userlist, che ritorna la lista degli utenti da visualizzare
 * delete, che elimina l'utente passato come parametro
 * toggleFollow, che aggiunge/rimuove la relazione di following verso un utente.


Informazioni memorizzate sul server e scambiate sulla rete
----------------------------------------------------------

Per il dettaglio, si vedano:

//TODO link to DTD o XML schema...

//TODO link alla javadoc di TweetListFile e UserListFile


Altre librerie
--------------

//TODO link al javadoc di ImageCache
