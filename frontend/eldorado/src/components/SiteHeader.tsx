import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu.tsx";
import {Button} from "@/components/ui/button.tsx";
import {CircleUser, Menu} from "lucide-react";
import {useAccountState} from "@/context/AccountContext.tsx";
import {RolesEnum} from "@/types/TokenPayload.ts";
import {Pathnames} from "@/router/pathnames.ts";
import {Link, useNavigate} from "react-router-dom";
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";
import {Sheet, SheetContent, SheetTrigger} from "@/components/ui/sheet.tsx";
import {Badge} from "@/components/ui/badge.tsx";

const SiteHeader = () => {
    const {account} = useAccountState();
    const navigate = useNavigate();
    const {t} = useTranslation();
    const accountLS = localStorage.getItem('account');
    const tokenLS = localStorage.getItem('token');
    const {logOut} = useAccount();

    function onClickLogout() {
        logOut();
    }

    function onClickSettings() {
        if (account) {
            navigate(Pathnames.loggedIn.accountSettings);
        }
    }

    function onClickChangeUserLevel() {
        if (account) {
            navigate(Pathnames.loggedIn.changeUserLevel);
        }
    }

    function onClickLogin() {
        navigate(Pathnames.public.login);
    }

    let headerColor = 'bg-gray-200 border-gray-200';
    if (account) {
        switch (account?.activeUserLevel?.roleName) {
            case RolesEnum.ADMIN:
                headerColor = 'bg-red-200 border-red-200';
                break;
            case RolesEnum.STAFF:
                headerColor = 'bg-blue-200 border-blue-200';
                break;
            case RolesEnum.CLIENT:
                headerColor = 'bg-green-200 border-green-200';
                break;
            default:
                headerColor = 'bg-black';
                break;
        }
    }

    const logo = (
        <>
            <svg version="1.0" xmlns="http://www.w3.org/2000/svg"
                 width="166.000000pt" height="40.000000pt" viewBox="0 0 2560.000000 617.000000"
                 preserveAspectRatio="xMidYMid meet">

                <g transform="translate(0.000000,617.000000) scale(0.100000,-0.100000)"
                   fill="#000000" stroke="none">
                    <path d="M11840 4006 l0 -176 308 0 c320 0 373 -5 494 -48 148 -52 305 -189
    370 -324 63 -128 82 -213 82 -363 0 -227 -60 -381 -204 -526 -91 -92 -159
    -136 -267 -174 -105 -36 -202 -45 -504 -45 l-279 0 0 -166 0 -165 398 3 397 4
    84 28 c349 116 573 317 697 625 58 145 76 238 77 396 1 310 -102 568 -308 771
    -158 156 -341 252 -580 306 -78 18 -136 21 -427 25 l-338 5 0 -176z"/>
                    <path d="M21160 4007 l0 -176 338 -3 c314 -4 342 -6 412 -27 124 -37 206 -86
    301 -181 96 -96 144 -176 181 -305 32 -114 32 -325 0 -438 -72 -250 -253 -432
    -501 -499 -68 -19 -110 -22 -403 -26 l-328 -4 0 -165 0 -164 398 3 397 3 109
    38 c319 110 522 285 651 561 53 113 72 175 90 296 24 157 17 288 -25 451 -102
    396 -417 685 -850 780 -95 20 -138 23 -437 27 l-333 4 0 -175z"/>
                    <path d="M7190 4000 l0 -170 810 0 810 0 0 170 0 170 -810 0 -810 0 0 -170z"/>
                    <path d="M9580 3095 l0 -1075 755 0 755 0 0 165 0 165 -570 0 -570 0 0 910 0
    910 -185 0 -185 0 0 -1075z"/>
                    <path d="M14755 4155 c-263 -58 -502 -218 -647 -432 -219 -326 -256 -735 -99
    -1101 99 -232 287 -423 520 -529 90 -41 207 -73 268 -73 l43 0 0 163 0 163
    -59 18 c-92 27 -178 83 -272 176 -98 97 -154 195 -190 335 -30 114 -30 307 -1
    427 59 243 238 438 470 513 l52 17 0 169 c0 129 -3 169 -12 168 -7 -1 -40 -7
    -73 -14z"/>
                    <path d="M15160 3998 l0 -173 73 -27 c214 -81 376 -263 439 -495 30 -111 30
    -325 -1 -438 -66 -248 -264 -457 -483 -509 l-28 -6 0 -167 0 -166 68 6 c110 9
    297 91 422 184 524 392 574 1216 102 1675 -148 144 -334 238 -559 283 l-33 7
    0 -174z"/>
                    <path d="M16410 4001 l0 -169 528 -5 c551 -4 558 -5 664 -50 56 -24 133 -101
    159 -160 34 -76 34 -230 0 -304 -27 -60 -112 -146 -168 -170 -108 -45 -114
    -46 -660 -50 l-523 -5 0 -534 0 -534 185 0 185 0 0 370 0 370 243 0 242 -1
    260 -369 259 -370 224 0 c178 0 222 3 215 13 -56 72 -564 789 -562 791 2 1 39
    15 82 30 135 49 239 127 318 239 87 124 127 299 109 475 -17 159 -67 269 -175
    378 -78 79 -161 130 -277 169 -149 49 -228 55 -790 55 l-518 0 0 -169z"/>
                    <path d="M19490 4166 c0 -3 101 -292 226 -642 188 -534 520 -1420 555 -1486 9
    -17 26 -18 209 -18 110 0 200 2 200 4 0 5 -753 2073 -771 2119 l-11 27 -204 0
    c-112 0 -204 -2 -204 -4z"/>
                    <path d="M24081 4155 c-139 -31 -293 -99 -406 -180 -220 -158 -380 -425 -425
    -707 -18 -112 -8 -370 19 -468 74 -273 219 -481 434 -624 135 -90 309 -155
    415 -156 l42 0 0 164 0 163 -72 24 c-204 65 -382 263 -444 494 -26 97 -27 360
    -1 455 41 149 142 299 260 387 61 45 176 103 230 117 l27 6 0 170 c0 94 -3
    170 -7 169 -5 0 -37 -7 -72 -14z"/>
                    <path d="M24482 3998 l3 -172 70 -26 c217 -81 378 -265 441 -505 28 -107 26
    -334 -4 -438 -42 -145 -118 -267 -229 -366 -57 -51 -190 -123 -250 -136 l-33
    -7 0 -165 0 -166 69 6 c80 7 218 57 321 117 252 147 427 386 502 687 19 75 22
    114 22 268 -1 161 -3 190 -27 278 -68 253 -233 493 -430 624 -116 77 -284 144
    -420 168 l-37 6 2 -173z"/>
                    <path d="M3545 3964 c-157 -10 -363 -27 -450 -39 -326 -45 -439 -93 -982 -407
    l-173 -100 -207 -9 c-115 -4 -252 -13 -306 -19 -473 -50 -857 -164 -1118 -332
    -65 -42 -66 -44 -75 -228 -5 -92 -2 -140 15 -240 l21 -125 -22 -30 c-12 -16
    -32 -44 -45 -62 l-24 -31 28 -6 c15 -3 111 -17 213 -31 102 -13 199 -27 216
    -30 l31 -5 -9 47 c-6 27 -10 102 -10 168 -1 198 41 323 139 416 29 27 72 59
    95 69 65 28 172 43 262 37 216 -16 341 -121 382 -321 18 -81 20 -256 4 -335
    -5 -29 -10 -58 -10 -62 0 -5 601 -9 1355 -9 l1355 0 -7 58 c-13 95 -7 271 11
    346 31 129 97 226 192 282 119 70 379 70 499 0 108 -64 178 -187 193 -343 8
    -84 2 -233 -13 -300 l-7 -32 63 4 c278 21 355 29 433 46 73 15 195 60 196 71
    1 2 11 113 24 248 l24 245 -50 94 -49 93 12 53 c13 59 8 270 -7 284 -5 5 -121
    30 -257 55 -246 46 -249 47 -365 105 -283 142 -635 274 -879 331 -184 43 -434
    59 -698 44z m545 -89 c281 -45 683 -186 1033 -362 105 -52 116 -55 340 -96
    l231 -43 7 -32 6 -32 -273 -2 -272 -3 72 -90 73 -90 190 -29 190 -29 46 -86
    46 -86 -20 -215 c-15 -167 -23 -218 -36 -227 -36 -27 -385 -83 -521 -83 l-28
    0 4 119 c11 317 -112 525 -348 586 -81 21 -238 21 -310 -1 -85 -25 -165 -72
    -215 -126 -82 -87 -144 -254 -145 -385 l0 -53 -1280 0 -1280 0 0 58 c0 60 -21
    176 -42 227 -6 17 -31 60 -55 97 -92 140 -264 208 -476 186 -201 -20 -335
    -131 -403 -333 -24 -69 -28 -101 -32 -233 l-4 -153 -63 6 c-35 4 -78 10 -96
    14 -34 7 -33 7 33 30 38 12 68 24 68 26 0 2 -54 82 -120 177 -112 162 -120
    176 -120 220 0 46 1 48 28 48 17 0 38 -10 52 -25 13 -14 27 -25 31 -25 5 0
    115 69 245 153 l236 152 -66 20 c-36 11 -66 22 -66 25 0 8 246 68 372 90 227
    41 528 70 720 70 l97 0 143 84 c581 339 681 381 1043 430 178 25 321 33 610
    35 191 1 285 -3 355 -14z m1615 -677 c-4 -24 -10 -46 -14 -51 -4 -4 -79 4
    -167 18 -203 32 -201 32 -214 55 -11 20 -7 20 196 20 l206 0 -7 -42z m-5016
    -80 c14 -5 20 -12 15 -17 -28 -27 -277 -181 -292 -181 -9 0 -28 9 -42 20 -14
    11 -35 20 -47 20 -13 0 -23 4 -23 9 0 13 132 89 229 132 86 37 102 39 160 17z
    m-311 -566 l53 -77 -32 -13 c-52 -22 -62 -14 -81 63 -31 133 -17 139 60 27z
    m3782 -157 l0 -45 -1285 0 c-1084 0 -1285 2 -1285 14 0 7 3 28 6 45 l7 31
    1278 0 1279 0 0 -45z"/>
                    <path d="M3322 3839 c-328 -25 -658 -129 -1002 -315 -98 -53 -259 -159 -310
    -203 l-35 -31 180 5 c99 4 238 10 309 16 72 5 177 9 235 9 58 0 189 5 291 10
    174 9 422 20 950 40 124 5 296 12 383 16 l158 7 70 95 c38 52 68 100 66 106
    -6 20 -190 106 -312 145 -66 21 -163 46 -215 55 -52 10 -115 22 -139 27 -46
    10 -372 29 -471 27 -30 0 -101 -4 -158 -9z m563 -79 c28 -5 95 -17 150 -25
    108 -18 112 -19 260 -65 115 -36 230 -89 230 -107 0 -7 -17 -33 -38 -58 l-38
    -45 -67 0 c-37 0 -137 -4 -222 -10 -266 -17 -772 -32 -781 -23 -7 7 56 285 77
    337 7 18 17 19 193 12 102 -4 209 -11 236 -16z m-495 -2 c0 -20 -79 -331 -86
    -337 -9 -9 -209 -18 -991 -43 -46 -2 -83 1 -84 6 -1 7 174 116 186 116 2 0 49
    22 106 48 223 105 403 160 684 211 79 14 185 13 185 -1z"/>
                    <path d="M19278 3659 c-279 -684 -658 -1624 -658 -1631 0 -4 89 -7 197 -6
    l196 3 233 567 232 567 -90 238 c-49 131 -93 247 -97 257 -6 15 -9 16 -13 5z"/>
                    <path d="M7190 3100 l0 -170 730 0 730 0 0 170 0 170 -730 0 -730 0 0 -170z"/>
                    <path d="M977 2960 c-358 -114 -386 -612 -43 -771 64 -30 80 -34 161 -34 81 0
    97 3 158 32 94 45 166 116 208 206 31 67 34 79 34 172 0 92 -3 106 -32 167
    -92 195 -294 290 -486 228z m248 -78 c77 -29 141 -88 182 -167 38 -72 43 -188
    13 -267 -28 -75 -96 -150 -168 -185 -51 -25 -67 -28 -162 -28 -98 0 -109 2
    -158 30 -116 64 -182 175 -182 308 0 234 248 396 475 309z"/>
                    <path d="M1049 2849 c-59 -9 -101 -31 -147 -77 -54 -54 -76 -105 -80 -190 -7
    -126 44 -218 149 -271 86 -43 161 -43 246 -1 209 105 206 409 -5 519 -35 18
    -111 28 -163 20z m156 -95 c50 -25 102 -100 110 -157 21 -137 -105 -260 -247
    -244 -114 14 -198 108 -198 222 0 72 70 166 145 193 45 17 144 10 190 -14z"/>
                    <path d="M4550 2957 c-127 -43 -230 -150 -266 -275 -22 -74 -15 -197 16 -275
    50 -130 198 -240 342 -254 112 -10 241 41 322 127 180 192 140 498 -82 639
    -100 63 -219 77 -332 38z m185 -58 c115 -21 211 -94 258 -197 31 -67 31 -207
    0 -274 -30 -66 -96 -133 -162 -165 -50 -25 -67 -28 -161 -28 -94 0 -110 3
    -155 27 -70 37 -123 92 -157 166 -27 57 -30 70 -26 150 2 67 9 98 28 137 44
    91 141 164 244 185 58 11 66 11 131 -1z"/>
                    <path d="M4626 2849 c-100 -15 -191 -100 -222 -207 -41 -140 40 -297 179 -349
    93 -34 216 -3 289 73 94 99 105 253 27 370 -57 83 -166 128 -273 113z m151
    -94 c128 -64 158 -226 61 -331 -99 -107 -277 -93 -351 28 -56 90 -46 190 26
    262 71 70 173 86 264 41z"/>
                    <path d="M7192 2188 l3 -163 808 -3 807 -2 0 165 0 165 -810 0 -810 0 2 -162z"/>
                </g>
            </svg>
            <span className="sr-only">Eldorado</span>
        </>
    )

    const navContent = (
        <>
            <Link to={Pathnames.public.home} className="flex items-center ">
                {logo}
            </Link>
            {account?.activeUserLevel?.roleName === RolesEnum.ADMIN && (
                <Link to={Pathnames.admin.userManagement}
                      className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                    {t("siteHeader.users")}
                </Link>
            )}
            {account?.activeUserLevel?.roleName === RolesEnum.ADMIN && (
                <Link to={Pathnames.admin.adminCreateUser}
                      className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                    {t("siteHeader.addUser")}
                </Link>
            )}
            {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                <Link to={Pathnames.client.myReservations}
                      className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                    {t("siteHeader.myReservations")}
                </Link>
            )}
            {(!account || account?.activeUserLevel?.roleName === RolesEnum.CLIENT) && (
                <Link to={Pathnames.client.parkingList}
                      className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                    {t("siteHeader.activeParkingList")}
                </Link>
            )}
            {account?.activeUserLevel?.roleName === RolesEnum.STAFF && (
                <Link to={Pathnames.staff.allReservations}
                      className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                    {t("siteHeader.allReservations")}
                </Link>
            )}
            {account?.activeUserLevel?.roleName === RolesEnum.STAFF && (
                <Link to={Pathnames.staff.parkingManagement}
                      className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap ">
                    {t("siteHeader.manageParking")}
                </Link>
            )}
            {(!account || account?.activeUserLevel?.roleName === RolesEnum.CLIENT) && (
                <Link to={Pathnames.public.enterParkingWithoutReservation}
                      className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                    {t("siteHeader.enterParking")}
                </Link>
            )}
            {account?.activeUserLevel?.roleName === RolesEnum.CLIENT && (
                <Link to={Pathnames.client.enterParkingWithReservation}
                      className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                    {t("siteHeader.reservationHistory")}
                </Link>
            )}
            {(!account || account?.activeUserLevel?.roleName === RolesEnum.CLIENT) && (
                <Link to={Pathnames.public.exitParking}
                      className="text-muted-foreground transition-colors hover:text-gray-500 whitespace-nowrap">
                    {t("siteHeader.exitParking")}
                </Link>
            )}
        </>
    )

    return (
        <header
            className={`top-0 flex h-16 items-center gap-4 border-b bg-background px-4 md:px-6 border-2 rounded-xl ${headerColor}`}>
            <nav
                className="hidden flex-col gap-6 text-lg font-medium md:flex md:flex-row md:items-center md:gap-5 md:text-sm lg:gap-6">
                {navContent}
            </nav>
            <Sheet>
                <SheetTrigger asChild>
                    <Button
                        variant="outline"
                        size="icon"
                        className="shrink-0 md:hidden"
                    >
                        <Menu className="h-5 w-5"/>
                        <span className="sr-only">Toggle navigation menu</span>
                    </Button>
                </SheetTrigger>
                <SheetContent side="left">
                    {<nav className="grid gap-6 text-lg font-medium whitespace-nowrap">
                        {navContent}
                    </nav>}
                </SheetContent>
            </Sheet>
            <div className="flex w-full items-center gap-4 justify-end">
                {(accountLS && tokenLS) && (
                    <>
                        <small
                            className="text-sm font-medium leading-none">{t("siteHeader.hello")}{account?.login}</small>
                        {//@ts-expect-error 😴😴😴😴😴😴
                            <Badge variant={"default"}>{t(account?.activeUserLevel?.roleName)}</Badge>
                        }
                        <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                                <Button variant="secondary" size="icon" className="rounded-full">
                                    <CircleUser className="h-5 w-5"/>
                                    <span className="sr-only">Toggle user menu</span>
                                </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="end">
                                <DropdownMenuLabel>{t("siteHeader.myAccount")}</DropdownMenuLabel>
                                <DropdownMenuSeparator/>
                                <DropdownMenuItem onClick={onClickSettings}>
                                    {t("siteHeader.settings")}
                                </DropdownMenuItem>
                                <DropdownMenuSeparator/>
                                <DropdownMenuItem onClick={onClickChangeUserLevel}>
                                    {t("siteHeader.changeLevel")}
                                </DropdownMenuItem>
                                <DropdownMenuSeparator/>
                                <DropdownMenuItem className="font-bold" onClick={onClickLogout}>
                                    {t("siteHeader.logout")}
                                </DropdownMenuItem>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    </>
                )}
                {(!accountLS || !tokenLS) && (
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button variant="secondary" size="icon" className="rounded-full">
                                <CircleUser className="h-5 w-5"/>
                                <span className="sr-only">Toggle user menu</span>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                            <DropdownMenuItem onClick={onClickLogin} className={"font-bold"}>
                                {t("siteHeader.login")}
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                )}
            </div>
        </header>
    );
}

export default SiteHeader;
