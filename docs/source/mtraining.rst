.. _mtraining-module:

================
mTraining Module
================

.. contents::
    :depth: 3

###########
Description
###########

The module provides data containers and APIs for defining training courses. The module gives possibility track user enrollments
and course progress. Courses structure can be very diverse thanks to the flexibility of the mTraining entities. Structure of the
Course may be created through the API or using the user interface.

################
Course Structure
################

Introduction
############

Course structure consists of units that will meet the objectives specified by the implementations. Every element of the
structure besides ``Questions`` contains following fields.

+------------+---------------------------------------------------------------------------------+--------------------+
|Field       |Description                                                                      |Type                |
+============+=================================================================================+====================+
|name        |Name of the course unit.                                                         |String              |
+------------+---------------------------------------------------------------------------------+--------------------+
|state       |Status of the course unit. It can take one of the values ``Active``,             |CourseUnitState     |
|            |``Inactive`` or ``Pending``.                                                     |                    |
+------------+---------------------------------------------------------------------------------+--------------------+
|content     |The content for the course unit. For example this could be an url to the audio   |String              |
|            |file.                                                                            |                    |
+------------+---------------------------------------------------------------------------------+--------------------+
|description |Description of the unit.                                                         |String              |
+------------+---------------------------------------------------------------------------------+--------------------+
|properties  |The additional properties which can be used with the unit. For example language, |Map<String, String> |
|            |version or district                                                              |                    |
+------------+---------------------------------------------------------------------------------+--------------------+

Course
######

``Course`` is a top level container which contains list of ``Chapters``.

Chapter
#######

``Chapters`` are the second level of the structure. Each ``Chapter`` can contain one ``Quiz`` and list of ``Lessons``.
``Chapters`` represent phase of the ``Course``.

Lesson
######

``Lesson`` object forms the leaf node of the ``Chapter``. For example it can contain information that help passing the ``Quiz``.

Quiz
####

``Quiz`` is used to store list of the ``Questions``. It also contains pass percentage(``passPercentage`` field of type
``double``) to pass the ``Chapter``;

Question
########

``Question`` contains two fields which link with the content in external system.

+----------+------------------------------------------------------------------------------------+--------------------+
|Field     |Description                                                                         |Type                |
+==========+====================================================================================+====================+
|question  |The question resource identifier in the external system, for example audio file.    |String              |
+----------+------------------------------------------------------------------------------------+--------------------+
|answer    |The answer resource identifier in the external system, for example audio file.      |String              |
|          |This field can also store the correct answer.                                       |                    |
+----------+------------------------------------------------------------------------------------+--------------------+

Bookmark
########

``Bookmark`` entity is used to store the user progress in the individual course units.

+------------------+------------------------------------------------------------------------------------+--------------------+
|Field             |Description                                                                         |Type                |
+==================+====================================================================================+====================+
|externalId        |The external id used by implementation to track user.                               |String              |
+------------------+------------------------------------------------------------------------------------+--------------------+
|courseIdentifier  |The identifier of the course.                                                       |String              |
+------------------+------------------------------------------------------------------------------------+--------------------+
|chapterIdentifier |The identifier of the chapter.                                                      |String              |
+------------------+------------------------------------------------------------------------------------+--------------------+
|lessonIdentifier  |The identifier of the lesson.                                                       |String              |
+------------------+------------------------------------------------------------------------------------+--------------------+
|progress          |The user progress. It allows to store more details relevant for the implementation. |Map<String, Object> |
+------------------+------------------------------------------------------------------------------------+--------------------+


Activity Record
###############

``Activity Record`` can be used either as the ``Bookmark``. It gives possibility to store user activity in the individual
course units, for example passing ``Quiz``.

+---------------+---------------------------------------------------------------------------------+--------------------+
|Field          |Description                                                                      |Type                |
+===============+=================================================================================+====================+
|externalId     |The external id used by implementation to track user.                            |String              |
+---------------+---------------------------------------------------------------------------------+--------------------+
|courseName     |Name of the ``Course``.                                                          |String              |
+---------------+---------------------------------------------------------------------------------+--------------------+
|chapterName    |Name of the ``Chapter``.                                                         |String              |
+---------------+---------------------------------------------------------------------------------+--------------------+
|lessonName     |Name of the ``Lesson``.                                                          |String              |
+---------------+---------------------------------------------------------------------------------+--------------------+
|quizName       |Name of the ``Quiz``.                                                            |String              |
+---------------+---------------------------------------------------------------------------------+--------------------+
|quizScore      |The result captured in the ``Quiz``.                                             |double              |
+---------------+---------------------------------------------------------------------------------+--------------------+
|startTime      |The start time of the user activity.                                             |DateTime            |
+---------------+---------------------------------------------------------------------------------+--------------------+
|completionTime |The end time of the user activity.                                               |DateTime            |
+---------------+---------------------------------------------------------------------------------+--------------------+
|state          |State of the user activity. It can take one of the values:``NONE``, ``STARTED``, |ActivityState       |
|               |``INPROGRESS`` or ``COMPLETED``.                                                 |                    |
+---------------+---------------------------------------------------------------------------------+--------------------+

#############
OSGI Services
#############

mTraining Service
#################

The mTraining service contains APIs to perform CRUD operations on ``Courses``, ``Chapters``, ``Lessons`` and ``Quizzes``.

- ``Course createCourse(Course course)``, ``Chapter createChapter(Chapter chapter)``, ``Lesson createLesson(Lesson lesson)``, ``Quiz createQuiz(Quiz quiz)`` - creates a course unit.
- ``Course updateCourse(Course course)``, ``Chapter updateChapter(Chapter chapter)``, ``Lesson updateLesson(Lesson lesson)``, ``Quiz updateQuiz(Quiz quiz)`` - updates the given course unit.
- ``void deleteCourse(long id)``, ``void deleteChapter(long id)``, ``void deleteLesson(long id)``, ``void deleteQuiz(long id)`` - removes a course unit with the given id.
- ``Course getCourseById(long id)``, ``Chapter getChapterById(long id)``, ``Lesson getLessonById(long id)``, ``Quiz getQuizById(long id)`` - returns a course unit by the given id.
- ``List<Course> getCoursesByName(String name)``, ``List<Chapter> getChaptersByName(String name)``, ``List<Lesson> getLessonsByName(String name)``, ``List<Quiz> getQuizzesByName(String name)`` - returns all course units with the given name.
- ``List<Chapter> getUnusedChapters()``, ``List<Lesson> getUnusedLessons()``, ``List<Quiz> getUnusedQuizzes()`` - returns all unused units (for example ``Lesson`` without ``Chapter``).
- ``List<Course> getCoursesByProperties(Map<String, String> properties)``, ``List<Chapter> getChaptersByProperties(Map<String, String> properties)``, ``List<Lesson> getLessonsByProperties(Map<String, String> properties)``, ``List<Quiz> getQuizzesByProperties(Map<String, String> properties)`` - returns all units that contains the given properties.
- ``List<Course> getAllCourses()``, ``List<Chapter> getAllChapters()``, ``List<Lesson> getAllLessons()``, ``List<Quiz> getAllQuizzes()`` - returns all unused units.
- ``Quiz getQuizForChapter(long id)`` - returns the quiz for a chapter with the given id.

Bookmark Service
################

Service for management of course bookmarks for a user. This is used to maintain the progress of a user in the curriculum.

- ``Bookmark createBookmark(Bookmark bookmark)`` - creates a bookmark for a user
- ``Bookmark getBookmarkById(long id)`` - returns a bookmark for the given id.
- ``Bookmark getLatestBookmarkByUserId(String externalId)`` - returns the latest bookmark for the user identified by the externalId.
- ``List<Bookmark> getAllBookmarksForUser(String externalId)`` - returns all bookmarks for the user identified by the externalId.
- ``Bookmark updateBookmark(Bookmark bookmark)`` - updates the given bookmark.
- ``void deleteBookmark(long id)`` - deletes a bookmark with the given id.
- ``void deleteAllBookmarksForUser(String externalId)`` - deletes all bookmarks for the user identified by the externalId.

Activity Record Service
#######################

Service for managing activity records.

- ``ActivityRecord createActivity(ActivityRecord activityRecord)`` - creates the given activity record.
- ``ActivityRecord updateActivity(ActivityRecord activityRecord)`` - updates the given activity record.
- ``ActivityRecord getActivityById(long activityId)`` - returns an activity record by the given id.
- ``List<ActivityRecord> getAllActivityForUser(String externalId)`` - returns all activity records for a user with the given externalId.
- ``List<ActivityRecord> getCompletedActivityForUser(String externalId)`` -  returns all completed activity records for user identified by the externalId.
- ``void deleteActivity(long activityRecordId)`` - deletes the activity record by the given id.
- ``void deleteAllActivityForUser(String externalId)`` - deletes all activity records for a user with the given externalId.

############
mTraining UI
############

The mTraining module has ``Courses``, ``Chapters``, ``Lessons`` and ``Quizzes`` tabs which allows to perform CRUD operations
on the course units. ``Bookmarks`` and ``Activity Record`` tabs allow to view logs. All those tab are using the MDS embedded UI.

Default tab is the ``Tree View`` tab. ``TreeView`` tab allows to manage relationships between course units. Colors of the units
represent the state(``blue`` - ``Active``, ``grey`` - ``Inactive``, ``orange`` - ``Pending``). You can easily change state of
the entire branch or a single unit, to do so use buttons below the tree. When you use ``Remove member`` button then
all children of the element will be disconnected(for example when you remove chapter then all ``Lessons`` will be disconnected from
``Chapter`` (each ``Lesson`` will be unused)). To add node to the tree you must select tree node to which the new node will be added
and then drag unit from ``Nodes`` section and drop it to the ``Members`` section. The ``Nodes`` column displays items that are
available to add.

            .. image:: img/mTrainingTreeView.png
                    :scale: 70 %
                    :alt: Edit Message
                    :align: center
