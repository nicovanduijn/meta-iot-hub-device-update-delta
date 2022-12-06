# Build and install the Microsoft GSL library

DESCRIPTION = "Microsoft Guidelines Support Library (GSL)"
AUTHOR = "Microsoft Corporation"
HOMEPAGE = "https://github.com/microsoft/GSL"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=363055e71e77071107ba2bb9a54bd9a7"

# We snap to public-preview-pnp for the latest and greates pnp changes.
SRC_URI = "gitsm://github.com/microsoft/GSL.git;branch=main"

# ADU Diff library requires tag v4.0.0
SRCREV = "a3534567187d2edc428efd3f13466ff75fe5805c"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE += "-DGSL_TEST=OFF"