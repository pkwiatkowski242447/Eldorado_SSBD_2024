import {useEffect, useState} from "react";
import {Spinner} from "react-bootstrap";
import {api} from "@/api/api.ts";
import {useTranslation} from "react-i18next";
import {arrayToDate, ClientReservationType, ReservationType} from "@/types/Reservations.ts";
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
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Slash} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";

function AllReservationsPage() {
    const {t} = useTranslation();
    const [reservations, setReservations] = useState<ClientReservationType[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [pageSize] = useState(4);

    const fetchAllReservations = async () => {
        setIsLoading(true);
        api.getAllReservations(currentPage, pageSize).then((response) => {
                const updatedReservations = response.data.map((reservation: ReservationType) => {
                    return {
                        ...reservation,
                        // @ts-expect-error ignore this for now
                        beginTime: arrayToDate(reservation.beginTime),
                        // @ts-expect-error ignore this for now
                        endingTime: reservation.endingTime ? arrayToDate(reservation.endingTime) : null
                    };
                });
                setReservations(updatedReservations);
                setIsLoading(false);
            }
        ).catch((error) => {
            handleApiError(error);
            setIsLoading(false);
        });
    }

    useEffect(() => {
        fetchAllReservations();
        console.log(reservations);
    }, [currentPage, pageSize]);

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/home">{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("allReservationsPage.title")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className={"grid gap-6"}>
                {isLoading ? (
                    <Spinner/>
                ) : (
                    <div className={"pt-5"}>
                        <Table className="p-10 flex-grow">
                            <TableHeader>
                                <TableRow className={"text-center p-10"}>
                                    <TableHead
                                        className="text-center">{t("allReservationsPage.table.clientID")}</TableHead>
                                    <TableHead
                                        className="text-center">{t("allReservationsPage.table.beginTime")}</TableHead>
                                    <TableHead
                                        className="text-center">{t("allReservationsPage.table.endingTime")}</TableHead>
                                    <TableHead
                                        className="text-center">{t("allReservationsPage.table.city")}</TableHead>
                                    <TableHead
                                        className="text-center">{t("allReservationsPage.table.street")}</TableHead>
                                    <TableHead
                                        className="text-center">{t("allReservationsPage.table.zipCode")}</TableHead>
                                    <TableHead
                                        className="text-center">{t("allReservationsPage.table.sectorName")}</TableHead>
                                    <TableHead
                                        className="text-center">{t("allReservationsPage.table.reservationId")}</TableHead>
                                </TableRow>
                            </TableHeader>
                            <TableBody className={"text-center"}>
                                {reservations.map(reservation => (
                                    <TableRow key={reservation.id} className="flex-auto">
                                        <TableCell>{reservation.clientId ? reservation.clientId : t("general.anonymous")}</TableCell>
                                        <TableCell>{reservation.beginTime}</TableCell>
                                        <TableCell>{reservation.endingTime}</TableCell>
                                        <TableCell>{reservation.city}</TableCell>
                                        <TableCell>{reservation.street}</TableCell>
                                        <TableCell>{reservation.zipCode}</TableCell>
                                        <TableCell>{reservation.sectorName}</TableCell>
                                        <TableCell>{reservation.id}</TableCell>
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
                                                if (currentPage > 0) setCurrentPage(currentPage - 1)
                                            }}
                                        />
                                    </PaginationItem>
                                    <PaginationItem>
                                        <PaginationLink>{currentPage + 1}</PaginationLink>
                                    </PaginationItem>
                                    <PaginationItem>
                                        <PaginationNext
                                            onClick={() => {
                                                if (reservations.length === pageSize) setCurrentPage(currentPage + 1)
                                            }}
                                        />
                                    </PaginationItem>
                                </PaginationContent>
                            </Pagination>
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}


export default AllReservationsPage;