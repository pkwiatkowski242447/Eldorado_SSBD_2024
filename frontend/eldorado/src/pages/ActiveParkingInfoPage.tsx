import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Loader2, Slash} from "lucide-react";
import {useTranslation} from "react-i18next";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {useEffect, useState} from "react";
import {ParkingListType, SectorInfoType, SectorType} from "@/types/Parking.ts";
import {useNavigate, useParams} from "react-router-dom";
import {Button} from "@/components/ui/button.tsx";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import {Pathnames} from "@/router/pathnames.ts";

function ActiveParkingInfoPage() {
    const [currentPage, setCurrentPage] = useState(() => parseInt("0"));
    const [parking, setParking] = useState<ParkingListType>();
    const [sector, setSector] = useState<SectorType[]>([]);
    const [selectedSector, setSelectedSector] = useState<SectorType | null>(null);
    const [sectorInfo, setSectorInfo] = useState<SectorInfoType | null>(null);
    const [isRefreshing, setIsRefreshing] = useState(false);
    const {t} = useTranslation();
    const {id} = useParams<{ id: string }>();
    const pageSize = 5;
    const navigate = useNavigate()

    const fetchParkingInfo = () => {
        if (id) {
            api.getParkingInfo(id)
                .then(response => {
                    setParking(response.data);
                })
                .catch(error => {
                    handleApiError(error);
                });
        }
    };

    const fetchParkingSectors = (page?: number) => {
        if (id) {
            const actualPage = page != undefined ? page : currentPage;
            const details = `?pageNumber=${actualPage}&pageSize=${pageSize}`;
            api.getParkingSectors(id, details)
                .then(response => {
                    if (response.status === 200) {
                        setCurrentPage(actualPage);
                        setSector(response.data);
                    }
                })
                .catch(error => {
                    handleApiError(error);
                });
        }
    };

    const fetchSectorInfo = (sectorId: string) => {
        if (sectorId) {
            api.getSectorInfo(sectorId)
                .then(response => {
                    setSectorInfo(response.data);
                })
                .catch(error => {
                    handleApiError(error);
                });
        }
    };

    const setActiveSector = (sector: SectorType) => {
        setSelectedSector(sector);
    };

    function handleButtonClick() {
        navigate(`/make-reservation/${selectedSector?.id}`)
    }

    useEffect(() => {
        fetchParkingInfo();
        fetchParkingSectors();
    }, []);

    useEffect(() => {
        if (selectedSector) {
            fetchSectorInfo(selectedSector.id);
        }
    }, [selectedSector]);

    function refresh() {
        setIsRefreshing(true);
        fetchParkingInfo();
        fetchParkingSectors();
        setTimeout(() => setIsRefreshing(false), 1000);
    }

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.public.home)}>{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.public.parkingList)}>{t("breadcrumb.active.parking.list")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.parking.info")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button onClick={refresh} variant={"ghost"} className="w-auto" disabled={isRefreshing}>
                    {isRefreshing ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("general.refresh")
                    )}
                </Button>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">{t("active.parking.info.page.parking.info")}{parking?.city}, {parking?.street}</h1>
            </div>
            <div>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead
                                className="text-center">{t("active.parking.info.page.city")}</TableHead>
                            <TableHead
                                className="text-center">{t("active.parking.info.page.street")}</TableHead>
                            <TableHead
                                className="text-center">{t("active.parking.info.page.zip.code")}</TableHead>
                            <TableHead
                                className="text-center">{t("active.parking.info.page.strategy")}</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        <TableRow key={parking?.id} className="flex-auto">
                            <TableCell>{parking?.city}</TableCell>
                            <TableCell>{parking?.street}</TableCell>
                            <TableCell>{parking?.zipCode}</TableCell>
                            {/* @ts-expect-error what? */}
                            <TableCell><Badge variant={"default"}>{t(parking?.strategy)} </Badge></TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">{t("active.parking.info.page.sectors")}</h1>
            </div>
            <div>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead
                                className="text-center">{t("active.parking.info.page.name")}</TableHead>
                            <TableHead
                                className="text-center">{t("active.parking.info.page.type")}</TableHead>
                            <TableHead
                                className="text-center">{t("active.parking.info.page.total.parking.spaces")}</TableHead>
                            <TableHead
                                className="text-center">{t("active.parking.info.page.free.parking.spaces")}</TableHead>
                            <TableHead className="text-center"></TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        {sector?.map(sector => (
                            <TableRow key={sector?.id} className="flex-auto">
                                <TableCell>{sector?.name}</TableCell>
                                <TableCell><Badge variant={"default"}>{t(sector?.type)} </Badge></TableCell>
                                <TableCell>{sector?.maxPlaces}</TableCell>
                                <TableCell>{sector?.occupiedPlaces}</TableCell>
                                <TableCell>
                                    <Dialog>
                                        <DialogTrigger>
                                            <Button onClick={() => setActiveSector(sector)}>{t("active.parking.info.page.view.info")}</Button>
                                        </DialogTrigger>
                                        <DialogContent>
                                            <DialogHeader>
                                                <DialogTitle className={"text-center"}>{t("active.parking.info.page.sector.info")}</DialogTitle>
                                                <DialogDescription className={"text-center p-3"}>
                                                    {sectorInfo && (
                                                        <>
                                                            <p><strong>{t("active.parking.info.page.name")}</strong> {sectorInfo.name}</p>
                                                            <p><strong>{t("active.parking.info.page.type")}</strong> {t(sectorInfo.type)}</p>
                                                            <p><strong>{t("active.parking.info.page.max.places")}</strong> {sectorInfo.maxPlaces}</p>
                                                            <Button className={"mt-5"} onClick={() => handleButtonClick()}>{t("active.parking.info.page.make.reservation")}</Button>
                                                        </>
                                                    )}
                                                </DialogDescription>
                                            </DialogHeader>
                                        </DialogContent>
                                    </Dialog>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <div className={"pt-5"}>
                    <Pagination>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious
                                    onClick={() => {
                                        if (currentPage > 0) fetchParkingSectors(currentPage - 1)
                                    }}
                                />
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationLink>{currentPage + 1}</PaginationLink>
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationNext
                                    onClick={() => {
                                        if (sector?.length === pageSize) fetchParkingSectors(currentPage + 1)
                                    }}
                                />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>
                </div>
            </div>
        </div>
    )
}

export default ActiveParkingInfoPage;
