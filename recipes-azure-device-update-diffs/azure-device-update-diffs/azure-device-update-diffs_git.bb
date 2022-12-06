SUMMARY = "bitbake-layers recipe"
DESCRIPTION = "Recipe created by bitbake-layers"
LICENSE = "CLOSED"

ADUC_DELTA_GIT_BRANCH ?= "main"

# Note: by default using a tarball of a snapshot of iot-hub-device-update-delta
#       from 'main' branch @ 57efe4360f52b297ae54323271c530239fb1d1c7
ADUC_DELTA_SRC_URI ?= "file://iot-hub-device-update-delta.tar.gz"
SRC_URI = "${ADUC_DELTA_SRC_URI}"

# This code handles setting variables for either git or for a local file.
# This is only while we are using private repos, once our repos are public,
# we will just use git:
# 
# e.g.,
#
#ADUC_DELTA_SRC_URI ?= "git://github.com/azure/io-thub-device-update-delta;branch=${ADUC_DELTA_GIT_BRANCH} \
#                       file://0001-Patch-for-ADU-Yocto-minimum-build.patch"
#
# branch: main, commit# 57efe4360f52b297ae54323271c530239fb1d1c7
# SRCREV = "57efe4360f52b297ae54323271c530239fb1d1c7"
# PV = "1.0+git${SRCPV}"
# S = "${WORKDIR}/git/src"

python () {
    src_uri = d.getVar('SRC_URI')
    if src_uri.startswith('git'):
        d.setVar('SRCREV', d.getVar('AUTOREV'))
        d.setVar('PV', '1.0+git' + d.getVar('SRCPV'))
        d.setVar('S', d.getVar('WORKDIR') + "/git/src")
    elif src_uri.startswith('file'):
        d.setVar('S',  d.getVar('WORKDIR') + "/iot-hub-device-update-delta/src")
}

DEPENDS = " ms-gsl bsdiff libgcrypt libgpg-error zlib zstd e2fsprogs"
RDEPENDS:${PN} += "bsdiff"

inherit cmake

# Build for Linux client.
EXTRA_OECMAKE += " -DUNIX=ON"

# Requires header file from bsdiff recipe.
do_compile[depends] += "bsdiff:do_prepare_recipe_sysroot"

# Suppress QA Issue: -dev package azure-device-update-diffs-dev contains non-symlink .so '/usr/lib/libadudiffapi.so' [dev-elf]
INSANE_SKIP:${PN} += " ldflags"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

# Publish the library.
FILES:${PN} += "${libdir}/libadudiffapi.*"