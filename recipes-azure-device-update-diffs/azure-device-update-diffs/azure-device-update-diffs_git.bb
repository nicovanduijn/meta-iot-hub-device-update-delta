SUMMARY = "bitbake-layers recipe"
DESCRIPTION = "Recipe created by bitbake-layers"
LICENSE = "CLOSED"

ADU_DELTA_GIT_BRANCH ?= "main"

ADU_DELTA_SRC_URI ?= "gitsm://github.com/azure/io-thub-device-update-delta"
SRC_URI = "${ADUC_DELTA_SRC_URI}b;ranch=${ADU_DELTA_GIT_BRANCH}"

ADU_DELTA_GIT_COMMIT ?= "57efe4360f52b297ae54323271c530239fb1d1c7"
SRCREV = "${ADU_DELTA_GIT_COMMIT}

PV = "1.0+git${SRCPV}"
S = "${WORKDIR}/git/src"

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