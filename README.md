Goal
====
Produce a simple web-app backend to complement the supplied front-end code.

The task
--------------


Imagine that you come back from 2 weeks of holidays on a Monday. On the team scrum board, assigned to you, two tasks await :


**User story 1:**

> **As a user, i want to be able to enter my expenses and have them saved for later.**

> _As a user, in the application UI, I can navigate to an expenses page. On this page, I can add an expense, setting :_

> 1. _The date of the expense_
> 0. _The value of the expense_
> 0. _The reason of the expense_

> _When I click "Save Expense", the expense is then saved in the database._
> _The new expense can then be seen in the list of submitted expenses._


**User story 2:**

> **As a user, I want to be able to see a list of my submitted expenses.**


> _As a user, in the application UI, i can navigate to an expenses page. On this page, I can see all the expenses I already submitted in a tabulated list.
> On this list, I can see :_

> 1. _The date of the expense_
> 0. _The VAT (Value added tax) associated to this expense. VAT is the UK’s sales tax. It is 20% of the value of the expense, and is included in the amount entered by the user._
> 0. _The reason of the expense_
>

By email, the front end developer of the team let you know that he already worked on the stories,  did build an UI and also went on holidays to France!

>_"Hi backEndDeveloper,_

>_Hope you had nice holidays.
>I did create an UI and prepared resources calls for those 2 user stories.
>You should only have to create the right endpoints in your service for the frontend application to call and everything should work fine!...
>Unless I forgot something of course, in which case you may be able to reach me on the beach_
>
>_PS. You can start the frontend by running `gulp` this will compile the code and launch a webserver on `localhost:8080`. You are free to host the files in your backend of course, then you will only need to build the source by running `gulp dev`. If you want to have a look at the code that is calling the endpoints then you can find this in `src/js/apps/codingtest/expenses/expenses-controller.js`_
>
>_PS II. In case you are stuck, you need to prep the project with `npm install -g gulp && npm install`. I'll leave it to you how to get Node on your system ;-)_
>
>_A bientôt !_
>
> _Robee_ & _Rinchen_
>"


**User story 3:**

> **As a user, I want to be able to save expenses in euros**

> _As a user, in the UI, when I write an expense, I can add the chars_ `EUR` _after it (example : 12,00 EUR).
> When this happens, the application automatically converts the entered value into pounds and save the value as pounds in the database.
The conversion needs to be accurate. It was decided that our application would call a public API to either realise the conversion or determine the right conversion rate, and then use it._



