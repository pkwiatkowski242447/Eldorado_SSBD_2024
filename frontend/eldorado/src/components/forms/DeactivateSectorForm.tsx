"use client";

import { format } from "date-fns";
import { pl, enUS } from "date-fns/locale";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormMessage,
} from "@/components/ui/form";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {TimePickerDemo} from "@/components/ui/time-picker-demo.tsx";
import {CalendarIcon, Loader2} from "lucide-react";
import {Calendar} from "@/components/ui/calendar.tsx";
import {useTranslation} from "react-i18next";
import {Dispatch, SetStateAction, useState} from "react";
import {
    AlertDialog,
    AlertDialogAction, AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription, AlertDialogFooter,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";

type deactivateSectorFormProps = {
    setDialogOpen: Dispatch<SetStateAction<boolean>>
    refresh: () => void
    sectorId: string
}

const formSchema = z.object({
    dateTime: z.date().min(new Date(),{message:"You can't disable parking in the past"}),
});

type FormSchemaType = z.infer<typeof formSchema>;

export function DeactivateSectorForm({setDialogOpen, refresh, sectorId}:deactivateSectorFormProps) {
    const [isLoading, setIsLoading] = useState(false);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [date, setDate] = useState<Date>(new Date());
    const {t} = useTranslation();
    const locale = window.navigator.language == "pl" ? pl : enUS;
    const form = useForm<FormSchemaType>({
        resolver: zodResolver(formSchema),
    });

    async function handleAlertDialog(){
        api.deactivateSector(sectorId, date)
            .then(() => {
                refresh();
                setAlertDialogOpen(false);
                setDialogOpen(false)})
            .catch(error => {
                setAlertDialogOpen(false);
                handleApiError(error);
            });
    }

    function onSubmit(data: FormSchemaType) {
        const deactivationTime = new Date(data.dateTime.valueOf() - (data.dateTime.getTimezoneOffset()));
        setIsLoading(true);
        try {
            setDate(deactivationTime);
            setAlertDialogOpen(true);
        } catch (error) {
            console.log(error);
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <Form {...form}>
            <form
                className="flex items-end gap-4 justify-center"
                onSubmit={form.handleSubmit(onSubmit)}
            >
                <FormField
                    control={form.control}
                    name="dateTime"
                    render={({ field }) => (
                        <FormItem className="flex flex-col">
                            <Popover>
                                <FormControl>
                                    <PopoverTrigger asChild>
                                        <Button
                                            variant="outline"
                                            className={cn(
                                                "w-[280px] justify-start text-left font-normal",
                                                !field.value && "text-muted-foreground"
                                            )}
                                        >
                                            <CalendarIcon className="mr-2 h-4 w-4" />
                                            {field.value ? (
                                                format(field.value, "PPP HH:mm", {locale:locale})
                                            ) : (
                                                <span>{t("general.pickDate")}</span>
                                            )}
                                        </Button>
                                    </PopoverTrigger>
                                </FormControl>
                                <FormMessage/>
                                <PopoverContent className="w-auto p-0">
                                    <Calendar
                                        locale={locale}
                                        mode="single"
                                        selected={field.value}
                                        onSelect={field.onChange}
                                        initialFocus
                                    />
                                    <div className="p-3 border-t border-border">
                                        <TimePickerDemo
                                            setDate={field.onChange}
                                            date={field.value}
                                        />
                                    </div>
                                </PopoverContent>
                            </Popover>
                            <Button type="submit" className="w-full" disabled={isLoading}>
                                {isLoading ? (
                                    <>
                                        <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                    </>
                                ) : (
                                    t("deactivate.sector.form.deactivate")
                                )}
                            </Button>
                        </FormItem>
                    )}
                />
            </form>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("deactivate.sector.form.are.you.sure.you.want.to.deactivate.this.sector")}
                    </AlertDialogDescription>
                    <AlertDialogFooter>
                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                        <AlertDialogAction onClick={handleAlertDialog}>
                            {t("general.ok")}
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </Form>
    );
}