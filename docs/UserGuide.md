---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# User Guide

ReserveMate is a **desktop application** designed for restaurant managers to manage reservations. It is optimized for use via a **Command Line Interface** (CLI), while still offering the benefits of a Graphical User Interface (GUI). If you can type fast, ReserveMate helps you complete reservation management tasks more quickly and efficiently than traditional GUI apps!

With ReserveMate, managing reservations becomes faster, more organized, and less error-prone. You can quickly locate bookings, and ensure customer preferences are met, helping you deliver a smooth and personalized dining experience.

This guide assumes you're comfortable using a computer and does not require any programming knowledge.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from [here](https://github.com/AY2425S2-CS2103-F08-1/tp/releases).

3. Copy the file to the folder you want to use as the _home folder_ for your ReserveMate.

4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar reservemate.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data. <br>

[//]: # (   ![Ui]&#40;images/UI.png&#41;)

[//]: # (5. Type the command in the command box and press `Enter` to execute it. e.g. typing **`help`** and pressing `Enter` will list all available commands.<br>)

[//]: # (   )
[//]: # (6. Refer to the [Features]&#40;#features&#41; below for details of each command.)

### GUI Overview

ReserveMate GUI is organized into three parts.

![Ui](images/Ui.png)

- `Command Box`: This is where users enter commands to interact with ReserveMate.
- `Reservation List`: Displays a list of all current reservations.
- `Result Display`: Shows output messages in response to user commands, such as confirmations, error messages or summaries of the executed commands.

### Command Structure

Understanding how commands work in ReserveMate is important for using the app effectively. This section breaks down the **structure, arguments, and parameters** used across all commands.

Commands in ReserveMate have the following structure:

<p style="text-align: center;">
    <code>command_word (REFERENCE) (PARAMETERS)</code>
</p>
<br>

| command_word                                                         | REFERENCE                                                 | PARAMETERS                                                     |
|----------------------------------------------------------------------|-----------------------------------------------------------|----------------------------------------------------------------|
| The command to be performed. Command words are **case-insensitive**. | Often used to reference an index in the reservation list. | Used to specify additional details for a given `command_word`. |

#### Reference Types

| Reference             | Meaning                                  | Constraints                       | Remarks                                                                      |
|-----------------------|------------------------------------------|-----------------------------------|------------------------------------------------------------------------------|
| `INDEX`<sup>1,2</sup> | Index of reservation in reservation list | Must be a positive integer `>= 1` | Used in commands like `edit` and `delete` to refer to a specific reservation |

**Notes:**

1. `INDEX` is **one-based** (i.e. starts from 1 not 0) and must fall within the range of the current reservation list.
2. ReserveMate handles `INDEX` errors in two ways:
   1. The index is a valid positive integer but exceeds the size of the current reservation list.
   2. The index is an invalid number (e.g. non-integer values, negative integers or zero), it is treated as an invalid index. Only values within the range `[1, reservation list size]` are supported.

#### Prefixes

Prefixes are in the format:

<p style="text-align: center;">
    <code>prefix/Value</code>
</p>

---
They come in several variations, based on whether they are **mandatory**, **optional**, or **repeatable** (variadic):

|                          | **Mandatory**        | **Optional<sup>1</sup>**     |
|--------------------------|----------------------|------------------------------|
| **Not variadic**         | `prefix/Value`       | `[prefix/Value]`             |
| **Variadic<sup>2</sup>** | `prefix/Value...`    | `[prefix/Value]...`          |
---

**Notes:**

1. **Optional prefixes** can be omitted, and the command will still execute successfully _(assuming all required parts of the command are provided and correctly formatted)_.
2. **Variadic prefixes** allow you to provide **multiple values** for the same field in a single command by repeating the prefix with different values.
  For example:
   - `o/Birthday o/Anniversary` → Valid (multiple occasions)

#### Prefix Types

The prefixes used in ReserveMate are universal across all commands.

| Prefix | Description             | Constraints                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | Valid                                      | Invalid                                       |
|--------|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------|-----------------------------------------------|
| `n/`   | Customer Name           | 2–50 characters, only `alphanumeric` characters and `spaces`. Case-insensitive.                                                                                                                                                                                                                                                                                                                                                                                                | `n/John Doe`, `n/Mary-Anne`, `n/Bobby Tan` | `n/J0hn`, `n/`, `n/@John`                     |
| `p/`   | Contact Number          | Exactly 8 digits and must start with `8` or `9`. It must be a Singapore phone number.                                                                                                                                                                                                                                                                                                                                                                                          | `p/91234567`, `p/87654321`                 | `p/1234567`, `p/01234567`, `p/`               |
| `e/`   | Email Address           | Must be a valid email format of `local-part@domain`. The local-part should only contain `alphanumeric` characters and these special characters, excluding the parentheses, `(+_.-)`. The local-part cannot start or end with any special characters. This is followed by a `@` and then a domain name. The domain name is made up of domain labels separated by periods. The final label (e.g., .com) must be at least 2 characters long and contain only letters (no digits). | `e/john@example.com`                       | `e/`, `e/john@.com`                           |
| `x/`   | Number of Diners        | Integer from 1 to 10 inclusive.                                                                                                                                                                                                                                                                                                                                                                                                                                                | `x/1`, `x/5`, `x/10`                       | `x/0`, `x/11`, `x/-2`, `x/ten`                |
| `d/`   | Reservation Date & Time | Format: `YYYY-MM-DD HHmm`. Must be within next 60 days. For `free` HHmm need not be included. For reservations, HHmm must be on the hour (e.g., 0000, 0100, etc.).                                                                                                                                                                                                                                                                                                             | `d/2025-05-11 1800`, `d/2025-04-30 1000`   | `d/2023-02-21`, `d/2028-02-21 0900`, `d/past` |
| `sd/`  | Start Date (Filter)     | Format: `YYYY-MM-DD HHmm`. Must be earlier than `ed/`.                                                                                                                                                                                                                                                                                                                                                                                                                         | `sd/2025-05-01 1800`                       | `sd/2025-13-01`, `sd/invalid`, `sd/`          |
| `ed/`  | End Date (Filter)       | Format: `YYYY-MM-DD HHmm`. Must be later than `sd/`.                                                                                                                                                                                                                                                                                                                                                                                                                           | `ed/2025-05-15 2200`                       | `ed/2025-01-01`, `ed/late`, `ed/`             |
| `o/`   | Occasion                | 2–50 characters, only `Alphanumeric` and common symbols (`- ' . , & ! ( ) /.`. It is `variadic`                                                                                                                                                                                                                                                                                                                                                                                | `o/Birthday`, `o/Anniversary o/VIP`        | `o/`, `o/@celebration`                        |

**Notes:**

1. Prefixes are case-insensitive: `n/John` is the same as `N/John`.
2. Prefix order does not matter in commands.
3. Optional prefixes may be omitted entirely.
4. Variadic prefixes (like `o/`) can appear multiple times in a command.
5. Blank values (e.g., `n/`, `p/`) are invalid and will return an invalid format error.
6. Use only the prefix stated in the table above to minimize unexpected behaviour.


### Remarks


---

#### `n/` — Reservation Name

- Names are **case-insensitive**:
<code>n/alex yeoh</code> is the same as <code>n/AlEx YeOh</code> it will be parsed as <code>n/Alex Yeoh</code>

- Names with **excessive leading/trailing spaces** are trimmed:
<code>n/   Alice Johnson</code> → <code>n/Alice Johnson</code>

- Names with **excessive spaces in between** are trimmed:
<code>n/Alex&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Yeoh</code> → <code>n/Alex Yeoh</code>

- Can be maximum 50 characters long.

- Names should contain only (english) characters and spaces.

- Names can be:
    - A **single character or initial** (e.g., `n/A`) — valid but potentially confusing in lists.

---

#### `p/` — Reservation Contact Number

- Phone numbers **must start with `8` or `9`** and be exactly 8 digits long (only Singapore phone numbers).
- Multiple reservations can share the **same phone number** and **different date-time**.

---

#### `e/` — Email Address

- Emails must match a **basic regex pattern**, but:
    - Technically valid emails may be **functionally incorrect** (e.g., `123@123`).
    - Strange but valid domains (e.g., `user@x-y.com`, `a@123.co`) are allowed.
    - Multiple reservations can share the **same email** and **different date-time**.

---

#### `x/` — Number of Diners

- Accepts integers from **1 to 10**, inclusive.
- Non-integer or out-of-range values (e.g., `x/0`, `x/15`, `x/one`) are rejected.

---

#### `d/` — Reservation Date & Time

- Format: `YYYY-MM-DD HHmm`
- Date must be:
    - Within the next **60 days**.
    - Cannot be a past date-time.
    - Time must be in hourly increments, ending with `00` (e.g., `1400`).
    - For the `free` command, `HHmm` is omitted.
    - For the `edit` command, the date & time cannot be in the past.

---

#### `o/` — Occasion

- Prefix is **optional and variadic** (can appear multiple times).
- Must be between 2 and 50 characters long.
- Accepts only alphanumeric values and common symbols (`- ' . , & ! ( ) /.`).
- Blank values (e.g., `o/`) will clear the occasions for the specific reservation when used in `edit` command it will
result in an error when used in `add` command.
- `o/birthday` and `o/Birthday` are treated differently.

---

#### `sd/` and `ed/` — Start and End Date for Filtering

- Format: `YYYY-MM-DD HHmm`
- `sd/` must be **before** `ed/`
- Time must be in hourly increments, ending with `00` (e.g., `1400`).

---

To get started with ReserveMate, type the command in the command box and press `Enter` to execute it.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Items in `<UPPER_CASE>` are mandatory parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in `[UPPER_CASE]` are optional parameters to be supplied by the user.<br>
  e.g. in `edit <INDEX> p/96214711`, `PHONE_NUMBER` is a parameter which can be used as `add n/John Doe p/96214711`.

* Items with `…` are variadic, meaning they can be used zero or more times.<br>
  e.g. `[o/OCCASION]…​` can be used as (i.e. 0 times), `o/Birthday`, `o/Birthday o/Graduation` etc.

* Prefix order does not matter. <br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be invalid.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as invalid.

* All commands are case-insensitive.
  e.g. if the command specifies `list` or `LIST` will be accepted as valid commands.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

**Notes on Execution Inputs and outputs:**

* The results of executing a command may **vary depending on the current state** of the reservation list.
* This includes both:
  * The **complete reservation list** (all reservations stored), and 
  * The **filtered reservation list** (what is currently visible after a find or list command). 
* For example, commands like `delete 1 cfm` or `edit 2` may be acting on the filtered list, depending on your previous commands.




### Viewing User Guide : `User Guide`

Refers user to GitHub ReserveMate user guide documentation.

![help message](images/userGuideMessage.png)


### Adding a reservation: `add`

Adds a new `Reservation` to ReserveMate.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL x/NUMBER_OF_DINER d/DATE_TIME [o/OCCASION]…`

**Constraints**
* A reservation can have any number of occasion (including 0).
* Phone number should start with either 8 or 9 and must be 8 digits.
* Date time should be after current time but within 60 days from it.

Notes:
* ReserveMate allows users to add multiple reservations. 
* However, duplicate reservations are not allowed.
* A reservation is considered a **duplicate** if:
  * It has the same phone number or email address, and the same date and time as an existing reservation. 
* This ensures no overlapping reservations are made for the same person at the same time.

- **Successful Execution:**
> ---
>
> **Use Case #1**: Adding a reservation under `John Doe` with phone number `98765432`, email `johnd@example.com`, diner size of `5` on `2025-04-12 1800` for a `birthday`.
>
> **Input**: `add n/John Doe p/98765432 e/johnd@example.com x/5 d/2025-04-12 1800 o/BIRTHDAY`
>
> **Output**: <br>
> ```
> New reservation added:
> Name: John Doe
> Phone: 98765432
> Email: johnd@example.com
> Number of Diners: 5
> Date/Time: 2025-04-12 1800
> Preference: None
> Occasion: [BIRTHDAY]
>```
> ---
>
> **Use Case #2**: Adding a reservation under `Jane Doe` with phone number `81234567`, email `betsycrowe@example.com`, diner size of `3` on `2025-04-20 1800` for a `graduation`.
>
> **Input**: `add n/Jane Doe e/betsycrowe@example.com x/3 p/81234567 o/GRADUATION d/2025-04-20 1800`
>
> **Output**: <br>
> ```
> New reservation added:
> Name: Jane Doe
> Phone: 81234567
> Email: betsycrowe@example.com
> Number of Diners: 3
> Date/Time: 2025-04-20 1800
> Preference: None
> Occasion: [GRADUATION]
> ```

- **Failed Execution:**
> ---
>
> **User Error #1**: Missing `NAME` field
>
> **Input**: `add p/93828282 e/johnd@example.com x/5 d/2025-5-01 1800`
>
> **Output**: <br>
> ```
> Invalid command format!
> add: Adds a reservation to the reservation book.
>
> Parameters:
> - n/NAME
> - p/PHONE
> - e/EMAIL
> - x/NUMBER OF DINERS
> - d/DATETIME
> - [o/OCCASION]...
>
> Example:
> add n/John Doe p/98765432 e/johnd@example.com x/5 d/2025-04-28 1800 o/Birthday
> ```
>
> ---
>
> **User Error #2**: Missing `PHONE` field
>
> **Input**: `add n/John Doe e/johnd@example.com x/5 d/2025-05-01 1800 o/Birthday`
>
> **Output**: <br>
> ```
> Invalid command format!
> add: Adds a reservation to the reservation book.
>
> Parameters:
> - n/NAME
> - p/PHONE
> - e/EMAIL
> - x/NUMBER OF DINERS
> - d/DATETIME
> - [o/OCCASION]...
>
> Example:
> add n/John Doe p/98765432 e/johnd@example.com x/5 d/2025-04-28 1800 o/Birthday
> ```
>
> ---
>
> **User Error #3**: Missing `DINER SIZE` field
>
> **Input**: `add n/John Doe p/98765432 e/johnd@example.com d/2025-05-01 1800 o/Birthday`
>
> **Output**: <br>
> ```
> Invalid command format!
> add: Adds a reservation to the reservation book.
>
> Parameters:
> - n/NAME
> - p/PHONE
> - e/EMAIL
> - x/NUMBER OF DINERS
> - d/DATETIME
> - [o/OCCASION]...
>
> Example:
> add n/John Doe p/98765432 e/johnd@example.com x/5 d/2025-04-28 1800 o/Birthday
> ```
>
> ---
>
> **User Error #4**: Missing `DATE` field
>
> **Input**: `add n/John Doe p/98765432 x/5 o/Birthday`
>
> **Output**: <br>
> ```
> Invalid command format!
> add: Adds a reservation to the reservation book.
>
> Parameters:
> - n/NAME
> - p/PHONE
> - e/EMAIL
> - x/NUMBER OF DINERS
> - d/DATETIME
> - [o/OCCASION]...
>
> Example:
> add n/John Doe p/98765432 e/johnd@example.com x/5 d/2025-04-28 1800 o/Birthday
> ```
>
> ---
>
> **User Error #5**: Reservation already exists (duplicates)
>
> **Input**: `add n/John Doe p/98765432 e/johnd@example.com x/5 d/2025-04-12 1800 o/BIRTHDAY`
>
> **Output**:
> ```A reservation already exists for this customer (same email or phone) at the chosen date-time.```
>
> ---
---

### Editing a reservation : `edit`

Edits an existing `Reservation` in ReserveMate.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [d/DATE_TIME] [x/NUMBER_OF_DINERS] [o/OCCASION]…`

**Constraints**
* `INDEX` **must be a positive integer** referring to a valid reservation in the list.
* At least one of field (prefix) must be provided.
* Editing occasion replaces the existing list of occasions. Use `o/` or `o/<WHITE_SPACE>` with no value to clear.
* Date and time must be within 60 days from now and in the future, time must be in hourly increments.
* When editing a future reservation, the new date-time cannot be in the past.

- **Successful Execution:**
> ---
>
> **Use Case #1**: Edit phone and email of reservation at index `1`.
>
> **Input:**
> `edit 1 p/91234567 e/johndoe@example.com`
>
> **Output:**
> ```
> Edited Reservation:
> Name: John Doe
> Phone: 91234567
> Email: johndoe@example.com
> Number of Diners: 5
> Date/Time: 2025-04-05 0800
> Preference: Less Salt
> Occasion: [Anniversary], [Birthday]
> ```
>
> ---
>
> **Use Case #2**: Edit name and clear all occasions for reservation at index `2`.
>
> **Input:**
> `edit 2 n/Brittany o/`
>
> **Output:**
> ```
> Edited Reservation:
> Name: Brittany
> Phone: 98765432
> Email: johnd@example.com
> Number of Diners: 5
> Date/Time: 2025-04-12 1800
> Preference: None
> Occasion:
> ```
>
> ---
>
> **Use Case #3**: Edit date and number of diners for reservation at index `3`.
>
> **Input:**
> `edit 3 d/2025-04-25 2000 x/6`
>
> **Output:**
> ```
> Edited Reservation:
> Name: Jane Doe
> Phone: 81234567
> Email: betsycrowe@example.com
> Number of Diners: 6
> Date/Time: 2025-04-25 2000
> Preference: None
> Occasion: [GRADUATION]
> ```
>
> ---

---

- **Failed Execution:**
> ---
>
> **User Error #1**: No fields provided to edit.
>
> **Input:**
> `edit 1`
>
> **Output:**
> ```
> At least one field to edit must be provided.
> ```
>
> ---
>
> **User Error #2**: Invalid index (0).
>
> **Input:**
> `edit 0 p/91234567`
>
> **Output:**
> ```
> The reservation index must be within the reservation list range
> ```
>
> ---
>
> **User Error #3**: Reservation index does not exist.
>
> **Input:**
> `edit 99 n/Alex`
> _(Assuming only 5 reservations exist)_
>
> **Output:**
> ```
> The reservation index must be within the reservation list range
> ```
>
> ---
>
> **User Error #4**: Editing with invalid phone number.
>
> **Input:**
> `edit 1 p/12345678`
>
> **Output:**
> ```
> Phone numbers should only contain numbers, it should begins with either 8 or 9 and it must be exactly 8 digits long
> ```
>
> ---
>
> **User Error #5**: Editing with past date.
>
> **Input:**
> `edit 2 d/2023-01-01 1200`
>
> **Output:**
> ```
> You cannot change a future reservation date to a past date.
> DateTime should be of the format YYYY-MM-DD HHmm and adhere to the following constraints:
> 1. The date must be a valid calendar date.
> 2. The time must be in hourly increments (e.g., 0000, 0100, etc.).
> 3. The date-time must be after the current time but within 60 days from now.
> ```
>
> ---
> >
> **User Error #5**: Editing with invalid email.
>
> **Input:**
> `edit 2 e/123@123`
>
> **Output:**
> ```
> Emails should follow the format local-part@domain and meet the following rules:
> 1. The local-part may contain alphanumeric characters and these special characters (excluding parentheses): +_.-.
> It must not start or end with a special character, and be at most 64 characters long.
> 2. The domain must consist of labels separated by periods (e.g., 'example.com'), with the following:
>    - Each label must start and end with an alphanumeric character
>    - Labels may contain hyphens, but no other special characters
>    - The domain must be at most 255 characters long
>    - The final label (e.g., '.com', '.sg') must be at least 2 characters long and contain only letters (no digits).
> ```
> ---

### Deleting a reservation : `delete`

Delete the specified `Reservation` from ReserveMate.

Format: `delete <INDEX> cfm`

**Constraints**
* `INDEX` **must be a positive integer** referring to a valid reservation in the list.
* A confirmation flag 'cfm' is **required** **and case-sensitive** to successfully delete the reservation.

---

- **Successful Execution:**
> ---
>
> **Use Case #1**: Deleting the 2nd reservation after listing all.
>
> **Input:**
>
> <br> 1. `list`
> <br> 2. `delete 2 cfm`
>
> **Output:**
> ```
> Reservation 2 deleted successfully
> ```
>
> ---
>
> **Use Case #2**: Deleting a reservation found through a filtered list.
>
> **Input:**
> 
> <br> 1. `find Jane`
> <br> 2. `delete 1 cfm`
>
>
> **Output:**
> ```
> Reservation 1 deleted successfully
> ```
>
> ---

---

- **Failed Execution:**
> ---
>
> **User Error #1**: Missing confirmation flag.
>
> **Input:**
> `delete 1`
>
> **Output:**
> ```
> Are you sure you want to delete 1?
>
> Type 'delete 1 cfm'
> ```
>
> ---
>
> **User Error #2**: Invalid index (zero).
>
> **Input:**
> `delete 0 cfm`
>
> **Output:**
> ```
> Invalid command format!
> delete: Delete the reservation identified by the index number used in the reservation list.
>
> Parameters: INDEX (must be a positive integer)
>
> Example: delete 1 cfm
> ```
>
> ---
>
> **User Error #3**: Index out of bounds.
>
> **Input:**
> `delete 10 cfm`
> _(Assuming only 3 reservations exist)_
>
> **Output:**
> ```
> The reservation index must be within the reservation list range
> ```
>
> ---
>
> **User Error #4**: Confirmation flag misspelled.
>
> **Input:**
> `delete 1 confirm`
>
> **Output:**
> ```
> Are you sure you want to delete 1?
>
> Type 'delete 1 cfm'
> ```
>
> ---

### Managing reservation preferences : `pref`

Saves a `Reservation` preference in ReserveMate.

Format:
* To save a preference: `pref save <INDEX> <PREFERENCE_TEXT>`

**Notes**:
* `INDEX` **must be a positive integer** referring to a valid reservation in the list.
* `PREFERENCE_TEXT` can contain alphanumeric values and common symbols (`- ' . , & ! ( ) /.`)(E.g. include dietary needs, seating
  preferences, or other customer requests).
* Preference would be `None` by default.

---

- **Successful Execution:**
> ---
>
> **Use Case #1**: Saving a preference for Reservation 1.
>
> **Input:**
> `pref save 1 Window seat preferred, allergic to nuts`
>
> **Output:**
> ```
> Saved preference for reservation: 1
> ```
>
> ---
---

- **Failed Execution:**
> ---
>
> **User Error #1**: Saving without providing a preference text description.
>
> **Input:**
> `pref save 2`
>
> **Output:**
> ```
> Invalid command format!
> pref: Saves customer preferences for the reservation identified by the index number.
>
> Parameters for saving: pref save <INDEX> <PREFERENCE_TEXT>
>
> Ensure all parameters are entered and valid
> Example: pref save 1 No nuts, allergic to seafood
> ```
>
> ---
>
> **User Error #2**: Negative or invalid index in `save`.
>
> **Input:**
> `pref save -10`
>  OR **Input:**
> `pref save abc`
>
> **Output:**
> ```
> The reservation index must be a non-negative integer greater than 0!
> ```
>
> ---
>
> > **User Error #3**: Positive Index outside of reservation list range.
>
> **Input:**
> `pref save 10 less salty`
>
> **Output:**
> ```
> The reservation index must be within the reservation list range
> ```
> 
> ---
>
> > **User Error #4**: Preference text exceeds 50 characters 
>
> **Input:**
> `pref save 10 longerthanfiftycharacterslongerthanfiftycharacterslongerthanfiftycharacters
>
> **Output:**
> ```
> Preference text cannot exceed 50 characters.
> ```
> 
> ---
> 
> **User Error #5**: Invalid command.
>
> **Input:**
> `pref update 1 Vegan menu`
>
> **Output:**
> ```
> Invalid command format!
> pref: Saves customer preferences for the reservation identified by the index number.
>
> Parameters for saving: pref save <INDEX> <PREFERENCE>
>
> Ensure all parameters are entered and valid
> Example: pref save 1 No nuts, allergic to seafood
> ```
> 
> ---

### Listing all reservations : `list`

Shows a list of all `Reservation` in the ReserveMate.

Format: `list`

---

- **Successful Execution:**
> ---
>
> **Use Case #1**: Listing all reservations in the system.
>
> **Input:**
> `list`
>
> **Output:**
> ```
> Listed all reservations
> ```
>
> ---
>
> **Use Case #2**: Listing when no reservations exist.
>
> **Input:**
> `list`
>
> **Output:**
> ```
> No reservations found. Use the 'add' command to create a reservation
> or 'help' to view all commands.
> ```
>
> ---

---

- **Failed Execution:**
> ---
>
> **User Error #1**: Providing unnecessary arguments.
>
> **Input:**
> `list all`
>
> **Output:**
> ```
> Invalid command format!
> list: Lists all reservations in the reservation book.
>
> Example: list
> ```
>
> ---
>
> **User Error #2**: Accidental typo.
>
> **Input:**
> `lst`
>
> **Output:**
> ```
> Invalid command format!
> help: Shows program usage instructions.
> Example: help
> ```
>
> ---
---

### Showing reservation details : `show`

Show additional details of a specific `Reservation`.

Format: `show <INDEX>`

**Constraints**:
* `INDEX` **must be a positive integer** referring to a valid reservation in the list.

---

- **Successful Execution:**
> ---
>
> **Use Case #1**: Viewing the details of the first reservation in the list.
>
> **Input:**
> `show 1`
>
> **Output:**
> ```
> Details of Reservation:
> Name: John Doe
> Phone: 98765432
> Email: johnd@example.com
> Number of Diners: 5
> Date/Time: 2025-04-28 1800
> Preference: None
> Occasion: [Birthday]
> ```
>
> ---
>
> **Use Case #2**: Showing reservation details without any occasion.
>
> **Input:**
> `show 2`
>
> **Output:**
> ```
> Details of Reservation:
> Name: Jane Doe
> Phone: 81234567
> Email: betsycrowe@example.com
> Number of Diners: 3
> Date/Time: 2025-04-29 2000
> Preference: None
> Occasion: [None]
> ```
>
> ---

---

- **Failed Execution:**
> ---
>
> **User Error #1**: Providing an index of 0.
>
> **Input:**
> `show 0`
>
> **Output:**
> ```
> The reservation index must be within the reservation list range
> ```
>
> ---
>
> **User Error #2**: Providing an index larger than the list size.
>
> **Input:**
> `show 10`
> _(Assuming only 3 reservations exist)_
>
> **Output:**
> ```
> The reservation index must be within the reservation list range
> ```
>
> ---
>
> **User Error #3**: Omitting the index.
>
> **Input:**
> `show`
>
> **Output:**
> ```
> Invalid command format!
> show: Shows the reservation details identified by the index number used in the displayed reservation list.
>
> Parameters: INDEX (must be a positive integer) and shown in the list
>
> Example: show 1
> ```
>
> ---
>
> **User Error #4**: Inputting a non-numeric index.
>
> **Input:**
> `show first`
>
> **Output:**
> ```
> The reservation index must be within the reservation list range
> ```
>
> ---

### Locating reservations by name: `find`

Finds `Reservation` with names that contain any of the specified keywords.

Format: `find NAME [MORE_NAMES]`

**Constraints**
- The search is **case-insensitive**, similar to search platforms like Google or Contacts.
- The order of keywords does **not** matter.
- Only **full words** will be matched (e.g., `Han` will not match `Hans`).
- Searches are done on the **name field only**.
- Matches are based on **OR** logic (any one name match is sufficient).

---

- **Successful Execution:**
> ---
>
> **Use Case #1**: Finding a reservation by full name.
>
> **Input:**
> `find John`
>
> **Output:**
> ```
> 1 reservations listed!
> ```
>
> ---
>
> **Use Case #2**: Finding multiple matches with multiple keywords.
>
> **Input:**
> `find john jane`
>
> **Output:**
> ```
> 2 reservations listed!
> ```
>
> ---
>
> **Use Case #3**: Case-insensitive match.
>
> **Input:**
> `find john`
>
> **Output:**
> ```
> 1 reservations listed!
> ```
>
> ---

---

- **Failed Execution:**
> ---
>
> **User Error #1**: No keyword provided.
>
> **Input:**
> `find`
>
> **Output:**
> ```
> Invalid command format!
> find: Finds all reservations whose names contain any of the specified keywords (case-insensitive).
>
> Parameters: NAME [MORE_NAMES]...
>
> Example: find alice Bob Charlie
> ```
>
> ---
>
> **User Error #2**: Keywords do not match any reservations.
>
> **Input:**
> `find Michael`
>
> **Output:**
> ```
> No reservations found. Try using the full name. For example, use 'John' instead of just 'Jo'.
> ```
>
> ---
>
> **User Error #3**: Partial word search.
>
> **Input:**
> `find Han`
> _(Assuming only `Hans` exists)_
>
> **Output:**
> ```
> No reservations found. Try using the full name. For example, use 'John' instead of just 'Jo'.
> ```
>
> ---

### Filtering the reservations: `filter`

Filters `Reservation` between the specified date and time range.

Format: `filter sd/ DATE_TIME ed/ DATE_TIME`

**Notes**:
* `filter` accepts **any date range** .
* Reservations can only be made for dates within the next 60 days from the current date.
  * This means filtering for future dates beyond 60 days will not return upcoming reservations but can still be used to view historical data.

**Constraints**
- Filters all reservations between the given `DATE_TIME`s, inclusive of the `DATE_TIME` provided.
-`DATE_TIME` provided must be valid and follow the format: `YYYY-MM-DD HHmm`.
- The `DATE_TIME` provided for `sd/` must be before the date and time provided for `ed/`

---

- **Successful Execution:**
> ---
>
> **Use Case #1**: Filtering reservations from `2025-04-12 1400` to `2025-05-15 1400`.
>
> **Input:**
> `filter sd/ 2025-04-12 1400 ed/ 2025-05-15 1400`
>
> **Output:**
> ```
> Here are the available reservations for the date range.
> ```
>
> ---
>
> **Use Case #2**: No reservations in the given range.
>
> **Input:**
> `filter sd/ 2026-12-20 1200 ed/ 2026-12-22 1200`
>
> **Output:**
> ```
> No reservations found for the date range.
> ```
>
> ---

---

- **Failed Execution:**
> ---
>
> **User Error #1**: Start date is after end date.
>
> **Input:**
> `filter sd/ 2025-05-20 1400 ed/ 2025-04-18 1400`
>
> **Output:**
> ```
> Start date must be before end date
> ```
>
> ---
>
> **User Error #2**: Invalid date format.
>
> **Input:**
> `filter sd/ 2026/12/12 1400 ed/ 2026-12-15 1400`
>
> **Output:**
> ```
> DateTime must be in the format YYYY-MM-DD HHmm, must be a valid calendar date and the time must be in hourly increments.
> ```
>
> ---
>
> **User Error #3**: Missing one or both parameters.
>
> **Input:**
> `filter sd/ 2026-12-12 1400`
>
> **Output:**
> ```
> Invalid command format!
> filter: Filters all reservations made between the given date range.
>
> Parameters:
> - sd/START DATE - ed/END DATE
>
> Example: filter sd/ 2025-04-01 1800 ed/ 2025-04-28 1900
> ```
>
> ---

### Free reservations: `free`

Displays all available `Reservation` time slots in user specified day.

Format: `free <DATE>`

**Constraints**
- Date must be in `YYYY-MM-DD` format. Do not include `HHmm`.

**Note**
- Each reservation is 1 hour long. For example, if a time slot ends at `2025-04-28 1800`, 
it means the available <br> time is from `2025-04-28 1700` to `2025-04-28 1800`. 
So, you can add a reservation at `2025-04-28 1700`.

---

- **Successful Execution:**
> ---
>
> **Use Case #1**: Viewing available slots.
>
> **Input:**
> `free d/2025-04-28`
>
> **Output:**
> ```
> Available free time slots:
> - 2025-04-28 0000 to 2025-04-28 1800
> - 2025-04-28 1900 to 2025-04-29 0000
> ```
>
> ---

---

- **Failed Execution:**
> ---
>
> **User Error #1**: Missing date.
>
> **Input:**
> `free`
>
> **Output:**
> ```
> Invalid command format!
> free: Find all free time slots in a given day
>
> Parameters: d/DATE
>
> Example: free d/2025-05-01
> ```
>
> ---
>
> **User Error #2**: Typo in command.
>
> **Input:**
> `freeslot`
>
> **Output:**
> ```
> Invalid command format!
> help: Shows program usage instructions.
> Example: help
> ```
>
> ---
> > **User Error #3**: Invalid date format.
>
> **Input:**
> `free d/04-28-2025`
>
> **Output:**
> ```
> Date must be in the format YYYY-MM-DD and adhere to the following constraints:
> 1. The date must be a valid calendar date.
> 2. The date must be after the current date but within 60 days from now.
> ```
>
> ---

### Display reservation statistics : `stats`

Displays statistics of `Reservation` in ReserveMate.

Format: `stats`

---

- **Successful Execution:**
> ---
>
> **Use Case #1**: Generating statistics when reservations exist.
>
> **Input:**
> `stats`
>
> **Output:**
> *(Bar chart appears showing distribution of number of reservations by diner size.)*
>
> ![stats_command.png](images/statsCommand.png)
>
> ---

---

- **Failed Execution:**
> ---
>
> **User Error #1**: Command used when no reservations exist.
>
> **Input:**
> `stats`
>
> **Output:**
> ```
> There are no reservations to generate statistics from. Use the 'add' command to create a reservation
> ```
>
> ---
>
> **User Error #2**: Command typed with extra argument.
>
> **Input:**
> `stats now`
>
> **Output:**
> ```
> Invalid command format!
> stats: Displays statistics of all reservations in the reservation book.
>
> Example: stats
> ```
>
> ---

### Clearing all entries : `clear`

Clears all `Reservation` from the ReserveMate.

Format: `clear cfm`

**Constraints**
- The confirmation flag `cfm` is **mandatory** and **case-sensitive**.
- This action **cannot be undone**.
- Used with caution to reset the reservation list **completely**.


---

- **Successful Execution:**
> ---
>
> **Use Case #1**: Clearing all reservations with confirmation.
>
> **Input:**
> `clear cfm`
>
> **Output:**
> ```
> Reservation book has been cleared!
> ```
>
> ---

---

- **Failed Execution:**
> ---
>
> **User Error #1**: Missing confirmation flag.
>
> **Input:**
> `clear`
>
> **Output:**
> ```
> Are you sure you want to clear ALL reservations?
>
> Type 'clear cfm'
> Keyword 'cfm' is case sensitive
> ```
>
> ---
>
> **User Error #2**: Typo in confirmation flag.
>
> **Input:**
> `clear confirm`
>
> **Output:**
> ```
> Are you sure you want to clear ALL reservations?
>
> Type 'clear cfm'
> Keyword 'cfm' is case sensitive
> ```
>
> ---
>
> **User Error #3**: Command issued when list is already empty.
>
> **Input:**
> `clear cfm`
>
> **Output:**
> ```
> Reservation List is empty. No reservations found to clear!
> ```
>
> ---

### Displaying commands : `help`

Displays a list of available commands.

Format: `help`

---

- **Successful Execution:**
> ---
>
> **Use Case #1**: Displaying the help window.
>
> **Input:**
> `help`
>
> **Output:**
> ```
> List of Commands:
> 1. add - Enter a reservation
> 2. edit - Edit a reservation
> 3. pref - Saves a reservation preference
> 4. delete - Delete a reservation
> 5. show - Display reservation details
> 6. list - Display a list of all reservations
> 7. help - Display a list of available commands
> 8. find - Find reservations by names
> 9. stats - Display reservation statistics
> 10. free - Display all free time slots to the user
> 11. filter - Filters all reservations which are between the two dates provided by the user
> 12. clear - Delete all reservations
> 13. exit - Exit the program
> ```

---

- **Failed Execution:**
> ---
>
> **User Error #1**: Command typed with extra arguments.
>
> **Input:**
> `help now`
>
> **Output:**
> ```
> Invalid command format!
> help: Shows program usage instructions.
> Example: help
> ```
>
> ---
>
> **User Error #2**: Misspelled command.
>
> **Input:**
> `halp`
>
> **Output:**
> ```
> Invalid command format!
> help: Shows program usage instructions.
> Example: help
> ```
>
> ---

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

ReserveMate data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

ReserveMate data are saved automatically as a JSON file `[JAR file location]/data/reservemate.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, ReserveMate will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the ReserveMate to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous ReserveMate home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action         | Format, Examples                                                                                                                                                           |
|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add**        | `add n/NAME p/PHONE_NUMBER e/EMAIL x/NUMBER_OF_DINER d/DATE_TIME [o/OCCASION]…​`<br>e.g., `add n/John Doe p/98765432 e/johnd@example.com x/5 d/2025-04-16 1800 o/Birthday` |
| **Edit**       | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [x/NUMBER_OF_DINERS] [d/DATE_TIME] [o/OCCASION]…​`<br>e.g., `edit 2 n/James Lee e/jameslee@example.com`                    |
| **Delete**     | `delete INDEX cfm`<br>e.g., `delete 3 cfm`                                                                                                                                 |
| **Preference** | `pref save INDEX PREFERENCE_TEXT`<br>e.g., `pref save 1 Window seat preferred`                                                                                             |
| **List**       | `list`<br>e.g., `list`                                                                                                                                                     |
| **Show**       | `show INDEX`<br>e.g., `show 2`                                                                                                                                             |
| **Find**       | `find NAME [MORE_NAMES]`<br>e.g., `find James Jake`                                                                                                                        |
| **Filter**     | `filter sd/DATE_TIME ed/DATE_TIME`<br>e.g., `filter sd/2025-04-20 1400 ed/2025-05-05 1400`                                                                                 |
| **Free**       | `free d/DATE_TIME`<br>e.g., `free d/2025-04-28`                                                                                                                            |
| **Stats**      | `stats`<br>e.g., `stats`                                                                                                                                                   |
| **Clear**      | `clear cfm`<br>e.g., `clear cfm`                                                                                                                                           |
| **Help**       | `help`<br>e.g., `help`                                                                                                                                                     |
| **Exit**       | `exit`<br>e.g., `exit`                                                                                                                                                     |
