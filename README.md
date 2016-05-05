--------------

This repository contains source code for add-on modules intended to be used with the MOTECH Platform.

The MOTECH Platform is an open source enterprise software package that connects popular eHealth technologies to unlock new outcomes toward strengthening health systems. MOTECH has been deployed across the globe in numerous health domains including maternal and child health, treatment adherence, frontline worker education and information collection.

MOTECH consists of a core platform and optional modules, each providing use of a technology such as SMS or email, or access to an external system such as CommCare or OpenMRS. Implementers can choose to install one or more modules, and developers can extend MOTECH by writing new modules. The [MOTECH repository](http://github.com/motech/motech) contains the code for the core platform, which comes with a few essential modules. All other community-supported modules are housed in this (modules) repository. Documentation for all of the community-supported MOTECH modules can be found [here](http://docs.motechproject.org/en/latest/modules/index.html).

Interested in learning more about MOTECH? Try the following resources:
* [MOTECH Project Website](http://motechproject.org)
* [MOTECH Documentation](http://docs.motechproject.org)
* [Issue Tracker](https://applab.atlassian.net/projects/MOTECH/summary)
* [Mailing List](https://groups.google.com/forum/?fromgroups#!forum/motech-dev)

Installation
------------

In order to use any of the modules here, you'll first need to install MOTECH Platform. Check out the README in the [MOTECH repo](http://github.com/motech/motech) for some tips on getting started.

Once you have the Platform installed, there are two ways to install additional modules:
* Use the Admin UI to install them at runtime.
* Place them in the `~/.motech/bundles` directory and restart MOTECH. Note that doing a `mvn clean install` on any of our modules will place that module in the `~/.motech/bundles` directory automatically. Modules from that directory always override the ones contained in the war if their Bundle-Version and Bundle-SymbolicName are the same.

You may build any single module from this repository by doing a `mvn clean install` within the module's directory. You may build all of the modules by doing a `mvn clean install` from the top-level directory.

Contributing
------------

We welcome contributions from the open source community. For instructions on how to get started as a MOTECH contributor, please check out the [Contribute](http://docs.motechproject.org/en/latest/contribute/index.html) section of our documentation.

Disclaimer Text Required By Our Legal Team
------------------------------------------

Third party technology may be necessary for use of MOTECH 2.0. This agreement does not modify or abridge any rights or obligations you have in open source technology under applicable open source licenses.

Open source technology programs that are separate from MOTECH are provided as a courtesy to you and are licensed solely under the relevant open source license. Any distribution by you of code licensed under an open source license, whether alone or with MOTECH, must be under the applicable open source license.
