(function() {
    'use strict';

    /* Controllers */
    var controllers = angular.module('mtraining.controllers', []);

    controllers.controller('TreeViewController', function($scope, $http, Modal) {

        innerLayout({
            spacing_closed: 30,
            east__minSize: 200,
            east__maxSize: 350
        });

        $scope.clearState = function () {
            $scope.selectedState='Active';
            $scope.children = [];
            $scope.unsaved = false;
            $scope.unsavedCourses = [];
            $scope.treeData = [];
            $scope.nodeProperties = [];
            $scope.iterator = 0;
            $scope.chapters = [];
            $scope.lessons = [];
            $scope.quizzes = [];
            $scope.nodes = [];
        };

        $scope.onNodeChanged = function (node) {
            $scope.unsaved = true;
            $scope.safeApply();
            if (node) {
                var parentCourse, path = $scope.jstree.get_path(node, false, true);
                if (path && path.length > 1) {
                    parentCourse = path[1];
                    if ($scope.unsavedCourses.indexOf(parentCourse) === -1) {
                        $scope.unsavedCourses.push(parentCourse);
                    }
                }
            }
        };

        $scope.onChanged = function () {
            var selected = $scope.jstree.get_node($scope.jstree.get_selected());
            $scope.children = [];
            $scope.nodes = [];
            $scope.quizNodes = [];
            if (selected.children) {
                $.each(selected.children, function(idx, el) {
                    $scope.children.push($scope.jstree.get_node(el));
                });
            }
            if (selected.original && selected.original.type) {
                if (selected.original.type === "course") {
                    $scope.nodes = $scope.chapters;
                    $scope.childIcon = $scope.jstree.settings.types.chapter.icon;
                    $scope.childType = "chapter";
                } else if (selected.original.type === "chapter") {
                    $scope.nodes = $scope.lessons;
                    $scope.quizNodes = $scope.quizzes;
                    $scope.childIcon = $scope.jstree.settings.types.lesson.icon;
                    $scope.childType = "lesson";
                }
            }
            $scope.safeApply();
        };

        $scope.createNode = function (item, parent, level) {
            $scope.iterator = $scope.iterator + 1;
            $scope.treeData[$scope.iterator] = {
                "id" : $scope.iterator,
                "text" : item.name,
                "parent" : parent,
                "state" : {
                        opened : false,
                        disabled : false,
                        selected : false
                    },
                li_attr : { state: item.state },
                a_attr : {},
                "level" : level,
                "type" : item.type
            };

            $scope.nodeProperties[$scope.iterator] = item;
        };

        // Builds data in correct format for the jsTree from the json data
        $scope.buildTree = function (data, init, level, par) {
            var currentId;
            //if initialization
            if (init) {
                $scope.treeData[$scope.iterator] = {
                    "id" : $scope.iterator,
                    "text" : "mTraining",
                    "parent" : par,
                    "state" : {
                            opened : true,
                            disabled : false,
                            selected : false
                        },
                    li_attr : { state: 'Active' },
                    "level" : level,
                    "type" : "root"
                };
                par = 0;
                level = level + 1;
            }

            if (data === null) {
                return;
            }

            // child nodes
            data.forEach(function(item) {
                $scope.createNode(item, par, level);
                currentId = $scope.iterator;
                $scope.buildTree(item.units, false, level + 1, $scope.iterator);
                if (item.type === 'chapter' && item.quiz !== undefined && item.quiz !== null) {
                     $scope.createNode(item.quiz, currentId, level + 1);
                }
            });
        };

        $scope.renderTree = function () {
            var i;
            $('#jstree').jstree({
                "plugins" : ["state", "dnd", "search", "types"],
                "core" : {
                    'data' : $scope.treeData,
                    'check_callback' : function (operation, node, node_parent, node_position) {
                        if (operation === "move_node") {
                            if (node_parent.original && node.original.level === node_parent.original.level + 1) {
                                var children = node_parent.children;
                                if (node.type === 'quiz' && $scope.getQuiz(node_parent) !== null) {
                                    return false;
                                }
                                return true;
                            }
                            return false;
                        }
                    }
                },
                "types" : {
                    "root": {
                        "icon" : "glyphicon glyphicon-cloud"
                    },
                    "course": {
                        "icon" : "glyphicon glyphicon-folder-open"
                    },
                    "chapter": {
                        "icon" : "glyphicon glyphicon-list-alt"
                    },
                    "lesson": {
                        "icon" : "glyphicon glyphicon-book"
                    },
                    "quiz": {
                        "icon" : "glyphicon glyphicon-question-sign"
                    }
                }
            });
            // selection changed
            $('#jstree').on("changed.jstree", function (e, data) {
                $scope.onChanged();
            });
            //node moved
            $('#jstree').on("move_node.jstree", function (e, data) {
                var node = data.node;
                $scope.onNodeChanged(data.parent);
                $scope.onNodeChanged(data.old_parent);
            });
            $scope.jstree = $.jstree.reference('#jstree');
            $scope.safeApply();
        };


        $scope.loadStructure = function () {
            $http.get('../mtraining/courses')
            .success(function (data) {
                $scope.buildTree( data, true, 0, "#");
                $scope.renderTree();
                $scope.jstree.select_node(0);
                $scope.loadChapters();
                Modal.closeLoadingModal();
            })
            .error(function (data) {
                Modal.closeLoadingModal();
                Modal.motechAlert('mtraining.error.courses', 'mtraining.error.title');
            });
        };

        // Loads courses from server
        $scope.loadTree = function () {
            $scope.clearState();
            Modal.openLoadingModal();
            $('#jstree').jstree("destroy");
            $scope.loadStructure();
        };

        $scope.loadTree();

        $scope.loadChapters = function () {
            $http.get('../mtraining/chapters')
            .success(function (data) {
                $scope.chapters = data;
                $scope.loadLessons();
                $scope.loadQuizzes();
            })
            .error(function (data) {
                Modal.closeLoadingModal();
                Modal.motechAlert('mtraining.error.chapters', 'mtraining.error.title');
            });
        };

        $scope.loadLessons = function () {
            $http.get('../mtraining/lessons')
            .success(function (data) {
                $scope.lessons = data;
            })
            .error(function (data) {
                Modal.closeLoadingModal();
                Modal.motechAlert('mtraining.error.lessons', 'mtraining.error.title');
            });
        };

        $scope.loadQuizzes = function () {
            $http.get('../mtraining/quizzes')
            .success(function (data) {
                $scope.quizzes = data;
            })
            .error(function (data) {
                Modal.closeLoadingModal();
                Modal.motechAlert('mtraining.error.quizzes', 'mtraining.error.title');
            });
        };

        $scope.createCoursesStructureToSave = function () {
            var i, courses = [];
            if ($scope.unsavedCourses.length > 0) {
                for(i = 0; i < $scope.unsavedCourses.length; i += 1) {
                    courses[i] = $scope.nodeProperties[$scope.unsavedCourses[i]];
                    courses[i].units = $scope.createChaptersStructureToSave($scope.jstree.get_node($scope.unsavedCourses[i]));
                }
                return courses;
            }
            return null;
        };

        $scope.createChaptersStructureToSave = function (node) {
            var i, units, chapters = [];
            if (node.children.length > 0) {
                for(i = 0; i < node.children.length; i += 1) {
                   chapters[i] = $scope.nodeProperties[node.children[i]];
                   units = $scope.createLessonsStructureToSave($scope.jstree.get_node(node.children[i]));
                   chapters[i].units = units.lessons;
                   chapters[i].quiz = units.quiz;
                }
                return chapters;
            }
            return null;
        };

        $scope.createLessonsStructureToSave = function (node) {
            var i, j = 0, units = {
                    lessons : [],
                    quiz : null
                };
            if (node.children.length > 0) {
                for(i = 0; i < node.children.length; i += 1) {
                    if ($scope.nodeProperties[node.children[i]].type === 'quiz') {
                        units.quiz = $scope.nodeProperties[node.children[i]];
                    } else {
                        units.lessons[j] = $scope.nodeProperties[node.children[i]];
                        j += 1;
                    }
                }
                return units;
            }
            return units;
        };

        $scope.saveCourses = function () {
            var coursesToUpdate = $scope.createCoursesStructureToSave();
            if (coursesToUpdate !== null) {
                Modal.openLoadingModal();
                $http({
                    method: 'POST',
                    url: '../mtraining/updateCourses',
                    data: coursesToUpdate
                }).success(function (response) {
                    $scope.loadTree();
                    Modal.closeLoadingModal();
                })
                .error(function (response) {
                    Modal.closeLoadingModal();
                    Modal.motechAlert('mtraining.error.save', 'mtraining.error.title');
                });
            }
        };

        $scope.cancel = function () {
            $scope.loadTree();
        };

        $scope.removeMember = function() {
            var node = $scope.jstree.get_node($scope.jstree.get_selected());
            Modal.motechConfirm('mtraining.confirm.removeMember', 'mtraining.confirm', function (val) {
                if (val) {
                    $scope.deleteMember(node);
                }
            });
        };

        $scope.deleteMember = function (node) {
            var i;
            $scope.onNodeChanged(node);
            if (node.type === 'chapter' && node.children !== undefined) {
                for(i = 0; i < node.children.length; i+=1) {
                    //we must move children as unused to display them into units part
                    $scope.moveElement($scope.nodeProperties[node.children[i]]);
                    $scope.nodeProperties[node.children[i]] = [];
                    $scope.treeData[node.children[i]] = [];
                }
                $scope.jstree.delete_node(node.children);
            }
            $scope.moveElement($scope.nodeProperties[node.id]);
            $scope.nodeProperties[node.id] = [];
            $scope.treeData[node.id] = [];
            $scope.jstree.delete_node(node.id);
            $scope.safeApply();
        };

        $scope.moveElement = function (element) {
            if (element.type === 'quiz') {
                $scope.quizzes.push(element);
            } else if (element.type === 'lesson') {
                $scope.lessons.push(element);
            } else {
                $scope.chapters.push(element);
            }
        };

        $scope.isStateButtonDisabled = function (state) {
            var node;
            if ($scope.isButtonDisabled()) {
                return true;
            }
            node = $scope.jstree.get_node($scope.jstree.get_selected());
            if (node.li_attr.state !== state) {
                return false;
            }
            return true;
        };

        $scope.changeState = function (state) {
            var node = $scope.jstree.get_node($scope.jstree.get_selected());
            $scope.onNodeChanged(node);
            $scope.setState(node, $scope.jstree.get_node($scope.jstree.get_selected(), true), state);
        };

        $scope.setState = function (node, element, state) {
            $scope.nodeProperties[node.id].state = state;
            $scope.treeData[node.id].li_attr.state = state;
            node.li_attr.state = state;
            element.attr("state", state);
        };

        $scope.changePathState = function () {
            var node = $scope.jstree.get_node($scope.jstree.get_selected());
            $scope.onNodeChanged(node);
            $scope.changeChildrenState(node, $scope.selectedState);
        };

        $scope.changeChildrenState = function (node, state) {
            var i, children = node.children;
            if (node.children !== undefined) {
                for (i = 0; i < node.children.length; i+=1) {
                    $scope.changeChildrenState($scope.jstree.get_node(node.children[i]), state);
                }
            }
            $scope.setState(node, $scope.jstree.get_node(node.id, true), state);
        };

        $scope.isRemoveButtonDisabled = function () {
            var node;
            if ($scope.isButtonDisabled()) {
                return true;
            }
            node = $scope.jstree.get_node($scope.jstree.get_selected());
            if (node.type === 'course') {
                return true;
            }
            return false;
        };

        $scope.isButtonDisabled = function () {
            var idx, node;
            if ($scope.jstree === null || $scope.jstree === undefined) {
                return true;
            }
            idx = $scope.jstree.get_selected();
            if (idx === null || idx === undefined || idx.length === 0) {
                return true;
            }
            node = $scope.jstree.get_node($scope.jstree.get_selected());
            if (node.type === 'root') {
                return true;
            }
            return false;
        };

        $scope.getQuiz = function (node) {
            var i, children = node.children;
            for(i = 0; i < children.length; i+=1) {
                if ($scope.treeData[children[i]].type === 'quiz') {
                    return $scope.jstree.get_node($scope.treeData[children[i]].id);
                }
            }
            return null;
        };

        $scope.getMembersColumnHeader = function () {
            var idx, node;
            if ($scope.jstree === null || $scope.jstree === undefined) {
                return $scope.msg('mtraining.members');
            }
            idx = $scope.jstree.get_selected();
            if (idx === null || idx === undefined || idx.length === 0) {
                return $scope.msg('mtraining.members');
            }
            node = $scope.jstree.get_node($scope.jstree.get_selected());
            if (node.type === 'root') {
                return $scope.msg('mtraining.members');
            }

            return $scope.msg('mtraining.membersOf', node.text);
        };

        $scope.safeApply = function () {
            if (!$scope.$$phase) {
                $scope.$digest();
            }
        };

        //Drag and Drop
        function receiveEventHandler(event, ui) {
            var newId, parent, idx, item, cancelled, quiz = false;

            parent = $scope.jstree.get_node($scope.jstree.get_selected());
            idx = ui.item.attr('idx');
            $scope.onNodeChanged(parent);
            if (ui.item.attr('class').indexOf("quiz") >= 0) {
                item = $scope.quizNodes[idx];
                $scope.quizNodes.splice(idx, 1);
                quiz = $scope.getQuiz(parent);
                if (quiz !== null) {
                    $scope.deleteMember(quiz);
                }
            } else {
                item = $scope.nodes[idx];
                $scope.nodes.splice(idx, 1);
            }
            $scope.createNode(item, parent.original.id, parent.original.level + 1);
            newId =  $scope.jstree.create_node(parent.original.id, $scope.treeData[$scope.iterator], 'last', false, false);
            $scope.children.push($scope.jstree.get_node(newId));
            $scope.jstree.open_node(parent);
            $scope.onChanged();
            $scope.safeApply();
            ui.sender.sortable('cancel');
            return false;
        }

        $('.table-lightblue-nohover').removeClass('.table-lightblue');
        $('.draggable').sortable({
            connectWith: '.droppable',
            items: 'li',
            cursorAt: { left: 0, top: 0 },
            receive: receiveEventHandler
        });
    });
}());
