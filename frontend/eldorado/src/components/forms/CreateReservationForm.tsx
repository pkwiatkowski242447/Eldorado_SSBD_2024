"use client";

import {format} from "date-fns";
import {enUS, pl} from "date-fns/locale";
import {cn} from "@/lib/utils";
import {Button} from "@/components/ui/button";
import {Popover, PopoverContent, PopoverTrigger,} from "@/components/ui/popover";
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form";
import {z} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";

import {TimePickerDemo} from "@/components/ui/time-picker-demo";
import {CalendarIcon} from "lucide-react";
import {Calendar} from "@/components/ui/calendar";
import {useTranslation} from "react-i18next";

const formSchema = z.object({
    startDateTime: z.date().min(new Date(), {message: "You can't create a reservation which starts in the past"}),
    endDateTime: z.date().min(new Date(), {message: "You can't create a reservation which ends right now"}),
});

type FormSchemaType = z.infer<typeof formSchema>;

interface DateTimePickerFormProps {
    onSubmit: (beginTime: Date, endTime: Date) => void;
}

export function CreateReservationForm({onSubmit}: DateTimePickerFormProps) {
    const {t} = useTranslation();
    const locale = window.navigator.language == "pl" ? pl : enUS;
    const form = useForm<FormSchemaType>({
        resolver: zodResolver(formSchema),
    });

    function handleSubmit(data: FormSchemaType) {
        onSubmit(data.startDateTime, data.endDateTime);
    }

    return (
        <Form {...form}>
            <form
                className="flex flex-col gap-4 justify-center"
                onSubmit={form.handleSubmit(handleSubmit)}
            >
                <FormField
                    control={form.control}
                    name="startDateTime"
                    render={({field}) => (
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
                                            <CalendarIcon className="mr-2 h-4 w-4"/>
                                            {field.value ? (
                                                format(field.value, "PPP HH:mm:ss", {locale: locale})
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
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="endDateTime"
                    render={({field}) => (
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
                                            <CalendarIcon className="mr-2 h-4 w-4"/>
                                            {field.value ? (
                                                format(field.value, "PPP HH:mm:ss", {locale: locale})
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
                        </FormItem>
                    )}
                />
                <Button type="submit">{t("create.reservation.form.submit")}</Button>
            </form>
        </Form>
    );
}
