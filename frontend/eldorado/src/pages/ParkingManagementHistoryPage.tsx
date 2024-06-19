import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Pathnames} from "@/router/pathnames.ts";
import {Loader2, Slash} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {useEffect, useState} from "react";
import {api} from "@/api/api.ts";
import {HistoryParkingType} from "@/types/Parking.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {arrayToDate, ReservationType} from "@/types/Reservations.ts";
import {Badge} from "@/components/ui/badge.tsx";

function ParkingManagementHistoryPage() {
    const [parkingHistory, setParkingHistory] = useState<HistoryParkingType[]>([]);
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const {t} = useTranslation();
    const {id} = useParams<{ id: string }>();
    const navigate = useNavigate();
    const pageSize = 4;


    const fetchParkingHistory = (page?: number) => {
        const actualPage = page != undefined ? page : currentPage;
        const details = `?pageNumber=${actualPage}&pageSize=${pageSize}`;
        if (id) {
            api.getParkingHistory(id, details)
                .then(response => {
                    if (response.status === 200) {
                        setCurrentPage(actualPage);
                        const updatedParkingHistory = response.data.map((parkingHistory: ReservationType) => {
                            return {
                                ...parkingHistory,
                                // @ts-expect-error ignore this for now
                                modificationTime: parkingHistory.modificationTime ? arrayToDate(parkingHistory.modificationTime) : null,
                            };
                        });
                        console.log(updatedParkingHistory);
                        setParkingHistory(updatedParkingHistory);
                    } else if (response.status === 204 && actualPage > 0) {
                        fetchParkingHistory(actualPage - 1)
                    } else if (response.status === 204 && actualPage <= 0) {
                        setCurrentPage(0);
                        setParkingHistory([]);
                    }
                })
                .catch(error => {
                    handleApiError(error);
                });
        }
    };

    const refresh = () => {
        setIsRefreshing(true);
        fetchParkingHistory();
        setTimeout(() => setIsRefreshing(false), 1000);
    }

    useEffect(() => {
        fetchParkingHistory();
    }, []);

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer"
                                            onClick={() => navigate(Pathnames.public.home)}>{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer"
                                            onClick={() => navigate(Pathnames.staff.parkingManagement)}>{t("breadcrumb.manageParking")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.parking.history")}</BreadcrumbLink>
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
                <h1 className="text-3xl font-semibold">{t("parking.management.history.page.title")}</h1>
            </div>
            <div className={"pt-5"}>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead
                                className="text-center">{t("parking.management.history.page.id")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.history.page.version")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.history.page.city")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.history.page.street")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.history.page.zip.code")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.history.page.strategy")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.history.page.modification.time")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.history.page.modified.by")}</TableHead>

                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        {parkingHistory.map(parkingHistory => (
                            <TableRow key={parkingHistory.id} className="flex-auto">
                                <TableCell>{parkingHistory.id}</TableCell>
                                <TableCell>{parkingHistory.version}</TableCell>
                                <TableCell>{parkingHistory.city}</TableCell>
                                <TableCell>{parkingHistory.street}</TableCell>
                                <TableCell>{parkingHistory.zipCode}</TableCell>
                                <TableCell><Badge variant={"default"}>{t(parkingHistory.strategy)} </Badge></TableCell>
                                <TableCell>{parkingHistory.modificationTime}</TableCell>
                                <TableCell>{parkingHistory.modifiedBy || t("general.anonymous")}</TableCell>
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
                                        if (currentPage > 0) fetchParkingHistory(currentPage - 1)
                                    }}
                                />
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationLink>{currentPage + 1}</PaginationLink>
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationNext
                                    onClick={() => {
                                        if (parkingHistory.length === pageSize) fetchParkingHistory(currentPage + 1)
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

export default ParkingManagementHistoryPage